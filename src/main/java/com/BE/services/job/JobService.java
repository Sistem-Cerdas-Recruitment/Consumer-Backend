package com.BE.services.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.BE.constants.EndpointConstants;
import com.BE.constants.JobApplicationStatus;
import com.BE.constants.JobStatus;
import com.BE.dto.antiCheat.EvaluationDTO;
import com.BE.dto.interview.InterviewChatDTO;
import com.BE.dto.interview.InterviewChatHistoryDTO;
import com.BE.dto.job.JobResultDTO;
import com.BE.dto.job.JobStatusResponseDTO;
import com.BE.dto.job.PostJobRequestDTO;
import com.BE.dto.job.PostJobResponseDTO;
import com.BE.dto.job.application.JobApplicationDTO;
import com.BE.dto.job.application.JobApplicationRequestDTO;
import com.BE.dto.job.application.JobApplicationResultDTO;
import com.BE.dto.job.application.JobApplicationStatusResponseDTO;
import com.BE.dto.job.matching.JobMatchingDTO;
import com.BE.dto.job.matching.MatchingRequestDTO;
import com.BE.dto.job.matching.MatchingResponseDTO;
import com.BE.entities.CurriculumVitae;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.repositories.JobRepository;
import com.BE.repositories.projections.JobApplicationProjection.JobApplicationUserJobProjection;
import com.BE.services.CurriculumVitaeService;
import com.BE.services.user.UserService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class JobService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    UserService userService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobApplicationRepository jobApplicationRepository;

    @Autowired
    CurriculumVitaeService curriculumVitaeService;

    public Job find(UUID id) {
        return jobRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Job not found"));
    }

    public JobResultDTO findJob(UUID id, String username) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Job not found"));
        User user = userService.getUserByEmail(username);
        Optional<JobApplication> existingJobApplication = jobApplicationRepository.findByJobAndUser(job,
                user);
        JobResultDTO jobResponseDTO = JobResultDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .status(job.getStatus())
                .minYearsOfExperience(job.getYearsOfExperience())
                .majors(job.getMajors())
                .skills(job.getSkills())
                .userId(job.getUser().getId())
                .company(job.getUser().getName())
                .location(job.getLocation())
                .salary(job.getSalary())
                .advantages(job.getAdvantages())
                .additionalInfo(job.getAdditionalInfo())
                .mode(job.getMode())
                .type(job.getType())
                .experienceLevel(job.getExperienceLevel())
                .responsibilities(job.getResponsibilities())
                .requirements(job.getRequirements())
                .applicants(job.getApplicants())
                .offeredInterview(job.getOfferedInterview())
                .interviewed(job.getInterviewed())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .closedAt(job.getClosedAt())
                .applied(existingJobApplication.isPresent())
                .build();
        return jobResponseDTO;
    }

    public List<JobResultDTO> findAllOpenJobs(String username) {
        User user = userService.getUserByEmail(username);
        List<Job> jobs = jobRepository.findAllByStatus(JobStatus.OPEN,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "updatedAt")));
        Map<UUID, JobApplicationUserJobProjection> jobApplications = jobApplicationRepository.findAllByUser(user)
                .stream()
                .collect(Collectors.toMap(JobApplicationUserJobProjection::getJobId, Function.identity()));
        List<JobResultDTO> response = jobs.stream().map(job -> {
            JobResultDTO jobResponseDTO = JobResultDTO.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .status(job.getStatus())
                    .userId(job.getUser().getId())
                    .company(job.getUser().getName())
                    .location(job.getLocation())
                    .advantages(job.getAdvantages())
                    .salary(job.getSalary())
                    .createdAt(job.getCreatedAt())
                    .updatedAt(job.getUpdatedAt())
                    .applied(jobApplications.containsKey(job.getId()))
                    .build();
            return jobResponseDTO;
        }).collect(Collectors.toList());
        return response;
    }

    public List<JobResultDTO> findAllByUser(String username) {
        User user = userService.getUserByEmail(username);
        List<Job> jobs = jobRepository.findAllByUser(user,
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.DESC, "updatedAt")));
        List<JobResultDTO> response = jobs.stream().map(job -> {
            JobResultDTO jobResponseDTO = JobResultDTO.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .status(job.getStatus())
                    .description(job.getDescription())
                    .userId(job.getUser().getId())
                    .company(job.getUser().getName())
                    .location(job.getLocation())
                    .salary(job.getSalary())
                    .advantages(job.getAdvantages())
                    .additionalInfo(job.getAdditionalInfo())
                    .mode(job.getMode())
                    .type(job.getType())
                    .experienceLevel(job.getExperienceLevel())
                    .responsibilities(job.getResponsibilities())
                    .requirements(job.getRequirements())
                    .minYearsOfExperience(job.getYearsOfExperience())
                    .applicants(job.getApplicants())
                    .offeredInterview(job.getOfferedInterview())
                    .interviewed(job.getInterviewed())
                    .createdAt(job.getCreatedAt())
                    .updatedAt(job.getUpdatedAt())
                    .closedAt(job.getClosedAt())
                    .build();
            return jobResponseDTO;
        }).collect(Collectors.toList());
        return response;
    }

    public JobStatusResponseDTO updateJobStatus(UUID JobId, Boolean status, String username) {
        User user = userService.getUserByEmail(username);
        Job job = jobRepository.findById(JobId).orElseThrow(() -> new NoSuchElementException("Job not found"));

        if (job.getUser().getId().equals(user.getId())) {
            if (status) {
                job.setStatus(JobStatus.OPEN);
            } else {
                job.setStatus(JobStatus.CLOSED);
                job.setClosedAt(job.getUpdatedAt());
            }
            jobRepository.save(job);
            JobStatusResponseDTO jobStatusResponseDTO = new JobStatusResponseDTO("Success", job.getStatus());
            return jobStatusResponseDTO;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public List<JobApplicationResultDTO> findApplications(UUID jobId, String username) {
        User user = userService.getUserByEmail(username);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("Job not found"));
        if (job.getUser().getId().equals(user.getId())) {
            List<JobApplicationResultDTO> jobApplications = jobApplicationRepository.findByJob(job).stream()
                    .map(jobApplication -> {
                        JobApplicationResultDTO jobApplicationResultDTO = JobApplicationResultDTO.builder()
                                .id(jobApplication.getId())
                                .jobId(jobApplication.getJob().getId())
                                .jobTitle(jobApplication.getJob().getTitle())
                                .recruiterId(jobApplication.getJob().getUser().getId())
                                .relevanceScore(jobApplication.getRelevanceScore())
                                .isRelevant(jobApplication.getIsRelevant())
                                .interviewScore(jobApplication.getInterviewScore())
                                .recruiterName(jobApplication.getJob().getUser().getName())
                                .status(jobApplication.getStatus())
                                .userId(jobApplication.getUser().getId())
                                .userName(jobApplication.getUser().getName())
                                .build();
                        return jobApplicationResultDTO;
                    }).collect(Collectors.toList());
            return jobApplications;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public PostJobResponseDTO createJob(PostJobRequestDTO request, String username) {
        User user = userService.getUserByEmail(username);
        List<String> skills = Stream.concat(request.getResponsibilities().stream(), request.getRequirements().stream())
                .collect(Collectors.toList());
        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .majors(request.getMajors())
                .skills(skills)
                .yearsOfExperience(request.getMinYearsOfExperience())
                .salary(request.getSalary())
                .advantages(request.getAdvantages())
                .additionalInfo(request.getAdditionalInfo())
                .mode(request.getMode())
                .type(request.getType())
                .location(request.getLocation())
                .experienceLevel(request.getExperienceLevel())
                .responsibilities(request.getResponsibilities())
                .requirements(request.getRequirements())
                .user(user)
                .status(JobStatus.OPEN)
                .build();
        jobRepository.save(job);
        JobResultDTO jobResultDTO = JobResultDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .status(job.getStatus())
                .description(job.getDescription())
                .majors(job.getMajors())
                .skills(job.getSkills())
                .minYearsOfExperience(job.getYearsOfExperience())
                .salary(job.getSalary())
                .advantages(job.getAdvantages())
                .additionalInfo(job.getAdditionalInfo())
                .mode(job.getMode())
                .type(job.getType())
                .location(job.getLocation())
                .experienceLevel(job.getExperienceLevel())
                .responsibilities(job.getResponsibilities())
                .requirements(job.getRequirements())
                .userId(job.getUser().getId())
                .company(job.getUser().getName())
                .applicants(job.getApplicants())
                .offeredInterview(job.getOfferedInterview())
                .interviewed(job.getInterviewed())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .build();
        return PostJobResponseDTO.builder()
                .message("Job posted successfully")
                .job(jobResultDTO)
                .build();
    }

    public List<JobApplicationResultDTO> findApplications(String username) {
        User user = userService.getUserByEmail(username);
        List<JobApplicationResultDTO> jobApplications = jobApplicationRepository.findAllByUser(user).stream()
                .map(jobApplication -> {
                    JobApplicationResultDTO jobApplicationResultDTO = JobApplicationResultDTO.builder()
                            .id(jobApplication.getId())
                            .jobId(jobApplication.getJob().getId())
                            .jobTitle(jobApplication.getJob().getTitle())
                            .recruiterId(jobApplication.getJob().getUser().getId())
                            .recruiterName(jobApplication.getJob().getUser().getName())
                            .status(jobApplication.getStatus())
                            .userId(jobApplication.getUser().getId())
                            .userName(jobApplication.getUser().getName())
                            .build();
                    return jobApplicationResultDTO;
                }).collect(Collectors.toList());
        return jobApplications;
    }

    public JobApplication getJobApplication(UUID applicationId) {
        return jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Job application not found"));
    }

    public JobApplicationDTO getCandidateJobApplication(UUID applicationId, String username) {
        JobApplication jobApplication = getJobApplication(applicationId);
        JobApplicationDTO jobApplicationDTO = JobApplicationDTO.builder()
                .id(jobApplication.getId())
                .status(jobApplication.getStatus())
                .jobId(jobApplication.getJob().getId())
                .jobTitle(jobApplication.getJob().getTitle())
                .recruiterId(jobApplication.getJob().getUser().getId())
                .recruiterName(jobApplication.getJob().getUser().getName())
                .userId(jobApplication.getUser().getId())
                .userName(jobApplication.getUser().getName())
                .fileName(jobApplication.getCv().getFileName())
                .cvUrl(curriculumVitaeService.get(jobApplication.getCv().getId(), username))
                .build();
        if (jobApplication.getUser().getEmail().equals(username)) {
            return jobApplicationDTO;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public JobApplicationDTO getRecruiterJobApplication(UUID applicationId, String username) {
        JobApplication jobApplication = getJobApplication(applicationId);
        if (jobApplication.getJob().getUser().getEmail().equals(username)) {
            return JobApplicationDTO.builder()
                    .id(jobApplication.getId())
                    .status(jobApplication.getStatus())
                    .relevanceScore(jobApplication.getRelevanceScore())
                    .isRelevant(jobApplication.getIsRelevant())
                    .interviewScore(jobApplication.getInterviewScore())
                    .experience(jobApplication.getExperience())
                    .jobId(jobApplication.getJob().getId())
                    .jobTitle(jobApplication.getJob().getTitle())
                    .recruiterId(jobApplication.getJob().getUser().getId())
                    .recruiterName(jobApplication.getJob().getUser().getName())
                    .userId(jobApplication.getUser().getId())
                    .userName(jobApplication.getUser().getName())
                    .fileName(jobApplication.getCv().getFileName())
                    .cvUrl(curriculumVitaeService.get(jobApplication.getCv().getId(),
                            jobApplication.getUser().getEmail()))
                    .build();
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public String getApplicationCV(UUID applicationId, String username) {
        JobApplication jobApplication = getJobApplication(applicationId);
        if (jobApplication.getJob().getUser().getEmail().equals(username) || jobApplication.getUser().getEmail().equals(username)){
            return curriculumVitaeService.get(jobApplication.getCv().getId(), jobApplication.getUser().getEmail());
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public JobApplicationStatusResponseDTO updateApplicationStatus(UUID applicationId, Boolean status,
            String username) {
        JobApplication jobApplication = getJobApplication(applicationId);
        if (jobApplication.getJob().getUser().getEmail().equals(username)) {
            if (jobApplication.getStatus().equals(JobApplicationStatus.PENDING)) {
                if (status) {
                    jobApplication
                            .setStatus(JobApplicationStatus.AWAITING_INTERVIEW);
                    jobApplication.getJob().setOfferedInterview(jobApplication.getJob().getOfferedInterview() + 1);
                } else {
                    jobApplication
                            .setStatus(JobApplicationStatus.REJECTED);
                }
            } else if (jobApplication.getStatus().equals(JobApplicationStatus.EVALUATED)) {
                if (status) {
                    jobApplication
                            .setStatus(JobApplicationStatus.ACCEPTED);
                } else {
                    jobApplication
                            .setStatus(JobApplicationStatus.REJECTED);
                }
            } else {
                throw new IllegalArgumentException("Application status cannot be updated");
            }
            jobApplicationRepository.save(jobApplication);
            JobApplicationStatusResponseDTO jobApplicationStatusResponseDTO = new JobApplicationStatusResponseDTO(
                    jobApplication.getStatus());
            return jobApplicationStatusResponseDTO;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public JobApplicationDTO apply(JobApplicationRequestDTO request, String username) {
        User user = userService.getUserByEmail(username);
        Job job = jobRepository.findById(request.getJobId())
                .orElseThrow(() -> new NoSuchElementException("Job not found"));
        Optional<JobApplication> existingJobApplication = jobApplicationRepository.findByJobAndUser(job,
                user);
        CurriculumVitae cv = curriculumVitaeService.find(request.getCvId(), user);

        if (existingJobApplication.isEmpty()) {
            JobApplication jobApplication = JobApplication.builder()
                    .status(JobApplicationStatus.PENDING)
                    .isRelevant(false)
                    .interviewChatHistory(
                            InterviewChatHistoryDTO.builder().competencies(job.getSkills())
                                    .chatHistories(new ArrayList<>())
                                    .build())
                    .experience(request.getExperience())
                    .job(job)
                    .user(user)
                    .cv(cv)
                    .build();

            // jobApplicationRepository.save(jobApplication);

            // getMatching
            MatchingRequestDTO matchingRequestDTO = new MatchingRequestDTO(
                    request.getExperience(),
                    JobMatchingDTO.builder()
                            .minYoE(job.getYearsOfExperience())
                            .role(job.getTitle())
                            .jobDesc(job.getDescription())
                            .majors(job.getMajors())
                            .skills(job.getSkills())
                            .build());

            log.info(matchingRequestDTO);

            ResponseEntity<MatchingResponseDTO> response;

            try {
                response = restTemplate.postForEntity(
                        EndpointConstants.MATCHING_SERVICE + "/classify",
                        matchingRequestDTO, MatchingResponseDTO.class);
            } catch (Exception e) {
                throw new RestClientException("failed to get matching");
            }

            log.info(response.getStatusCode());

            MatchingResponseDTO responseBody = response.getBody();

            if (response.getStatusCode().is2xxSuccessful() && responseBody != null) {
                jobApplication.setRelevanceScore(responseBody.getRelevanceScore());
                jobApplication.setIsRelevant(responseBody.getIsRelevant());
            } else {
                throw new IllegalArgumentException("Failed to get matching");
            }

            job.setApplicants(job.getApplicants() + 1);
            jobRepository.save(job);
            jobApplicationRepository.save(jobApplication);

            return JobApplicationDTO.builder()
                    .id(jobApplication.getId())
                    .status(jobApplication.getStatus())
                    .jobId(jobApplication.getJob().getId())
                    .jobTitle(jobApplication.getJob().getTitle())
                    .recruiterId(jobApplication.getJob().getUser().getId())
                    .experience(request.getExperience())
                    .recruiterName(jobApplication.getJob().getUser().getName())
                    .userId(jobApplication.getUser().getId())
                    .userName(jobApplication.getUser().getName())
                    .fileName(jobApplication.getCv().getFileName())
                    .cvUrl(curriculumVitaeService.get(request.getCvId(), username))
                    .build();
        } else if (existingJobApplication.isPresent()) {
            throw new IllegalArgumentException("You have already applied for this job");
        } else {
            throw new NoSuchElementException("Job not found");
        }
    }

    public JobApplication updateJobApplicationInterview(UUID applicationId, JobApplicationStatus status,
            List<EvaluationDTO> evaluations) {
        JobApplication jobApplication = getJobApplication(applicationId);
        int chatHistorySize = jobApplication.getInterviewChatHistory().getChatHistories().stream()
                .mapToInt(List::size)
                .sum();
        if (evaluations.size() != chatHistorySize) {
            throw new IllegalArgumentException("Invalid number of evaluations");
        }
        jobApplication.setStatus(status);
        List<List<InterviewChatDTO>> interviewChatHistories = jobApplication.getInterviewChatHistory()
                .getChatHistories();

        int evaluationIndex = 0;
        for (List<InterviewChatDTO> interviewChatHistory : interviewChatHistories) {
            for (InterviewChatDTO chatHistory : interviewChatHistory) {
                chatHistory.setPredictedClass(evaluations.get(evaluationIndex).getPredictedClass());
                chatHistory.setConfidence(evaluations.get(evaluationIndex).getConfidence());
                chatHistory.setSecondaryModelPrediction(evaluations.get(evaluationIndex).getSecondaryModelProbability());
                chatHistory.setMainModelProbability(evaluations.get(evaluationIndex).getMainModelProbability());
                evaluationIndex++;
            }
        }

        return jobApplicationRepository.save(jobApplication);
    }

    public JobApplication save(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }

    public String delete(UUID jobId) {
        jobRepository.deleteById(jobId);
        return "Job deleted successfully";
    }
}
