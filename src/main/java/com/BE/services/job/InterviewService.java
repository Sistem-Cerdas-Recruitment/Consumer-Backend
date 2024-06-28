package com.BE.services.job;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.BE.constants.EndpointConstants;
import com.BE.constants.InterviewStatus;
import com.BE.constants.JobApplicationStatus;
import com.BE.dto.antiCheat.AntiCheatEvaluationDTO;
import com.BE.dto.interview.GenerateQuestionRequestDTO;
import com.BE.dto.interview.GenerateQuestionResponseDTO;
import com.BE.dto.interview.InterviewChatDTO;
import com.BE.dto.interview.InterviewChatHistoryDTO;
import com.BE.dto.interview.InterviewDTO;
import com.BE.dto.interview.InterviewEvaluationDTO;
import com.BE.dto.interview.InterviewResponseDTO;
import com.BE.entities.JobApplication;
import com.BE.services.antiCheat.AntiCheatService;
import com.BE.services.kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.security.RolesAllowed;

@Service
public class InterviewService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private JobService jobService;

    @Autowired
    private AntiCheatService antiCheatService;

    public InterviewDTO getInterview(UUID jobApplicationId, String username) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        if (jobApplication.getUser().getUsername().equals(username)
                && jobApplication.getStatus().equals(JobApplicationStatus.INTERVIEW)) {
            return new InterviewDTO(InterviewStatus.ON_GOING, jobApplication.getInterviewChatHistory());
        } else if (jobApplication.getJob().getUser().getUsername().equals(username)) {
            return new InterviewDTO(null, jobApplication.getInterviewChatHistory());
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }
    
    public void evaluteInterview(InterviewEvaluationDTO interviewEvaluationDTO) {
        try {
            String payload = objectMapper.writeValueAsString(interviewEvaluationDTO);
            kafkaProducer.sendMessage("interview-evaluation", payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }

    public InterviewResponseDTO answer(UUID jobApplicationId, String username, InterviewChatDTO chatLog) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        if (jobApplication.getUser().getUsername().equals(username)
                && jobApplication.getStatus().equals(JobApplicationStatus.INTERVIEW)) {
            InterviewChatHistoryDTO chatHistory = jobApplication.getInterviewChatHistory();
            int competencyIndex = chatHistory.getCompetencyIndex();
            // log.info("Competency Index: {}, last chat history: [{}]", competencyIndex, chatHistory.getChatHistories().get(competencyIndex).size());
            InterviewChatDTO interviewChatLogDTO = chatHistory.getChatHistories().get(competencyIndex)
                    .get(chatHistory.getChatHistories().get(competencyIndex).size() - 1);

            // Check if the question is the same
            if (!interviewChatLogDTO.getQuestion().equals(chatLog.getQuestion())) {
                throw new IllegalArgumentException("Question does not match");
            }

            interviewChatLogDTO.setAnswer(chatLog.getAnswer());
            interviewChatLogDTO.setBackspaceCount(chatLog.getBackspaceCount());
            interviewChatLogDTO.setLetterClickCounts(chatLog.getLetterClickCounts());

            GenerateQuestionRequestDTO generateQuestionRequestDTO = new GenerateQuestionRequestDTO(
                    chatHistory.getCompetencies().get(chatHistory.getCompetencyIndex()),
                    chatHistory.getChatHistories().get(competencyIndex));
            ResponseEntity<GenerateQuestionResponseDTO> response = restTemplate.postForEntity(
                    EndpointConstants.INTERVIEW_SERVICE + "/transcript", generateQuestionRequestDTO,
                    GenerateQuestionResponseDTO.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RestClientException("Failed to generate next interview question");
            }
            GenerateQuestionResponseDTO generateQuestionResponseDTO = response.getBody();
            if (generateQuestionResponseDTO != null) {

                if (!generateQuestionResponseDTO.getStatus()) {
                    // Not Finished
                    chatHistory.getChatHistories().get(competencyIndex)
                            .add(InterviewChatDTO.builder().question(generateQuestionResponseDTO.getResponse()).build());
                    jobService.save(jobApplication);
                    return new InterviewResponseDTO(InterviewStatus.ON_GOING, generateQuestionResponseDTO.getResponse());
                } else {
                    // Finished
                    chatHistory.setCompetencyIndex(competencyIndex + 1);
                    if (chatHistory.getCompetencyIndex() == chatHistory.getCompetencies().size()) {
                        jobApplication.setStatus(JobApplicationStatus.AWAITING_EVALUATION);

                        AntiCheatEvaluationDTO antiCheatEvaluationDTO = AntiCheatEvaluationDTO.builder()
                                .jobApplicationId(jobApplicationId)
                                .data(chatHistory.getChatHistories().stream().flatMap(List::stream).toList())
                                .build();
                        antiCheatService.checkForCheating(antiCheatEvaluationDTO);

                        InterviewEvaluationDTO interviewEvaluationDTO = InterviewEvaluationDTO.builder()
                                .jobApplicationId(jobApplicationId)
                                .competences(chatHistory.getCompetencies())
                                .transcript(chatHistory.getChatHistories())
                                .build();
                        evaluteInterview(interviewEvaluationDTO);

                        jobService.save(jobApplication);
                        return new InterviewResponseDTO(InterviewStatus.COMPLETED, "Interview Completed");
                    } else {
                        chatHistory.getChatHistories().add(new ArrayList<>());
                        GenerateQuestionRequestDTO generateQuestionRequestDTO2 = new GenerateQuestionRequestDTO(
                                chatHistory.getCompetencies().get(chatHistory.getCompetencyIndex()),
                                chatHistory.getChatHistories().get(chatHistory.getCompetencyIndex()));
                        ResponseEntity<GenerateQuestionResponseDTO> response2 = restTemplate.postForEntity(
                                EndpointConstants.INTERVIEW_SERVICE + "/transcript", generateQuestionRequestDTO2,
                                GenerateQuestionResponseDTO.class);

                        if (!response2.getStatusCode().is2xxSuccessful()) {
                            throw new RestClientException("Failed to generate next interview question");
                        }

                        GenerateQuestionResponseDTO responseBody = response2.getBody();
                        if (responseBody != null) {
                            chatHistory.getChatHistories().get(competencyIndex)
                                    .add(InterviewChatDTO.builder().question(responseBody.getResponse()).build());
                            jobService.save(jobApplication);
                            return new InterviewResponseDTO(InterviewStatus.ON_GOING, responseBody.getResponse());
                        } else {
                            throw new RestClientException("Failed to get response body");
                        }
                    }
                }
            } else {
                throw new RestClientException("Failed to get response body");
            }
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public InterviewResponseDTO start(UUID jobApplicationId, String username) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        if (jobApplication.getUser().getUsername().equals(username)
                && jobApplication.getStatus().equals(JobApplicationStatus.AWAITING_INTERVIEW)) {
            InterviewChatHistoryDTO chatHistoryDTO = jobApplication.getInterviewChatHistory();

            List<InterviewChatDTO> chat = new ArrayList<>();
            GenerateQuestionRequestDTO generateQuestionRequestDTO = new GenerateQuestionRequestDTO(
                    chatHistoryDTO.getCompetencies().get(chatHistoryDTO.getCompetencyIndex()), chat);
            ResponseEntity<GenerateQuestionResponseDTO> response = restTemplate.postForEntity(
                    EndpointConstants.INTERVIEW_SERVICE + "/transcript", generateQuestionRequestDTO,
                    GenerateQuestionResponseDTO.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to start interview");
            }

            GenerateQuestionResponseDTO responseBody = response.getBody();

            if (responseBody == null) {
                throw new RuntimeException("Failed to get response body");
            } else{
                chat.add(InterviewChatDTO.builder().question(responseBody.getResponse()).build());
                chatHistoryDTO.getChatHistories().add(chat);
                jobApplication.setStatus(JobApplicationStatus.INTERVIEW);
                jobService.save(jobApplication);
                return new InterviewResponseDTO(InterviewStatus.ON_GOING, responseBody.getResponse());
            }

        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }

    }

    @RolesAllowed("ADMIN")
    public void score(UUID jobApplicationId, Float score) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        jobApplication.getJob().setInterviewed(jobApplication.getJob().getInterviewed() + 1);
        jobApplication.setInterviewScore(score);
        jobService.save(jobApplication);
    }

}
