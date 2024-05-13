package com.BE.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.BE.constants.InterviewStatus;
import com.BE.constants.JobApplicationStatus;
import com.BE.dto.InterviewChatLogDTO;
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
            return new InterviewDTO(InterviewStatus.ON_GOING, jobApplication.getInterviewChatLogs());
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public InterviewChatLogDTO answer(UUID jobApplicationId, String username, InterviewChatLogDTO chatLog) {
        JobApplication jobApplication = jobService.getJobApplication(jobApplicationId);
        if (jobApplication.getUser().getUsername().equals(username)
                && jobApplication.getStatus().equals(JobApplicationStatus.INTERVIEW)) {
            InterviewChatLogDTO interviewChatLogDTO = jobApplication.getInterviewChatLogs()
                    .get(jobApplication.getInterviewChatLogs().size() - 1);
            interviewChatLogDTO.setAnswer(chatLog.getAnswer());
            interviewChatLogDTO.setBackspaceCount(chatLog.getBackspaceCount());
            interviewChatLogDTO.setLetterClickCounts(chatLog.getLetterClickCounts());
            interviewChatLogDTO.setTypingDuration(chatLog.getTypingDuration());

            jobService.save(jobApplication);
            return chatLog;
            // TODO: Request for the next question

        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }
}
