package com.BE.services.job;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.BE.constants.InterviewStatus;
import com.BE.constants.JobApplicationStatus;
import com.BE.dto.InterviewChatDTO;
import com.BE.dto.InterviewDTO;
import com.BE.entities.JobApplication;

@Service
public class InterviewService {

    @Autowired
    private JobService jobService;

    public InterviewDTO getInterview(UUID jobApplicationId, String username) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        if (jobApplication.getUser().getUsername().equals(username)
                && jobApplication.getStatus().equals(JobApplicationStatus.INTERVIEW)) {
            return new InterviewDTO(InterviewStatus.ON_GOING, jobApplication.getInterviewChatHistory());
        } else if (jobApplication.getJob().getUser().getUsername().equals(username)) {
            return new InterviewDTO(null, jobApplication.getInterviewChatHistory());
        }else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public InterviewChatDTO answer(UUID jobApplicationId, String username, InterviewChatDTO chatLog) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        if (jobApplication.getUser().getUsername().equals(username)
                && jobApplication.getStatus().equals(JobApplicationStatus.INTERVIEW)) {
            InterviewChatDTO interviewChatLogDTO = jobApplication.getInterviewChatHistory()
                    .get(jobApplication.getInterviewChatHistory().size() - 1);
            interviewChatLogDTO.setAnswer(chatLog.getAnswer());
            interviewChatLogDTO.setBackspaceCount(chatLog.getBackspaceCount());
            interviewChatLogDTO.setLetterClickCounts(chatLog.getLetterClickCounts());

            jobService.save(jobApplication);
            return chatLog;
            // TODO: Request for the next question

        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }
}
