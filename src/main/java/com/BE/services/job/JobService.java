package com.BE.services.job;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.BE.constants.JobApplicationStatus;
import com.BE.constants.JobStatus;
import com.BE.dto.InterviewChatDTO;
import com.BE.dto.antiCheat.EvaluationDTO;
import com.BE.dto.job.JobApplicationDTO;
import com.BE.dto.job.JobApplicationResultDTO;
import com.BE.dto.job.JobResultDTO;
import com.BE.entities.CurriculumVitae;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.repositories.JobRepository;
import com.BE.repositories.projections.JobApplicationProjection.JobApplicationUserJobProjection;
import com.BE.services.CurriculumVitaeService;
import com.BE.services.user.UserService;
import com.BE.repositories.projections.JobProjection;

@Service
public class JobService {

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

    public JobResultDTO findJob(UUID id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Job not found"));
        JobResultDTO jobResponseDTO = JobResultDTO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .description(job.getDescription())
                .userId(job.getUser().getId())
                .name(job.getUser().getName())
                .build();
        return jobResponseDTO;
    }

    public List<JobResultDTO> findAllOpenJobs(String username) {
        User user = userService.getUserByEmail(username);
        List<JobProjection> jobs = jobRepository.findAllByStatus(JobStatus.OPEN,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "updatedAt")));
        Map<UUID, JobApplicationUserJobProjection> jobApplications = jobApplicationRepository.findAllByUser(user)
                .stream()
                .collect(Collectors.toMap(JobApplicationUserJobProjection::getJobId, Function.identity()));
        List<JobResultDTO> response = jobs.stream().map(job -> {
            JobResultDTO jobResponseDTO = JobResultDTO.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .userId(job.getUser().getId())
                    .name(job.getUser().getName())
                    .applied(jobApplications.containsKey(job.getId()))
                    .build();
            return jobResponseDTO;
        }).collect(Collectors.toList());
        return response;
    }

    public List<JobResultDTO> findAllByUser(String username) {
        User user = userService.getUserByEmail(username);
        List<JobProjection> jobs = jobRepository.findAllByUser(user,
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.DESC, "updatedAt")));
        List<JobResultDTO> response = jobs.stream().map(job -> {
            JobResultDTO jobResponseDTO = JobResultDTO.builder()
                    .id(job.getId())
                    .title(job.getTitle())
                    .description(job.getDescription())
                    .userId(job.getUser().getId())
                    .name(job.getUser().getName())
                    .build();
            return jobResponseDTO;
        }).collect(Collectors.toList());
        return response;
    }

    public List<JobApplicationResultDTO> findApplications(UUID jobId, String username) {
        User user = userService.getUserByEmail(username);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("Job not found"));
        if (job.getUser().getId().equals(user.getId())) {
            List<JobApplicationResultDTO> jobApplications = jobApplicationRepository.findByJob(job).stream()
                    .map(jobApplication -> {
                        JobApplicationResultDTO jobApplicationResultDTO = JobApplicationResultDTO.builder()
                                .id(jobApplication.getId())
                                .jobId(jobApplication.getJobId())
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
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public Job createJob(String title, String description, String username) {
        User user = userService.getUserByEmail(username);
        Job job = Job.builder()
                .title(title)
                .description(description)
                .user(user)
                .status(JobStatus.OPEN)
                .build();
        return jobRepository.save(job);
    }

    public List<JobApplicationResultDTO> findApplications(String username) {
        User user = userService.getUserByEmail(username);
        List<JobApplicationResultDTO> jobApplications = jobApplicationRepository.findAllByUser(user).stream()
                .map(jobApplication -> {
                    JobApplicationResultDTO jobApplicationResultDTO = JobApplicationResultDTO.builder()
                            .id(jobApplication.getId())
                            .jobId(jobApplication.getJobId())
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
                .cvUrl(curriculumVitaeService.get(applicationId, username))
                .build();
        if (jobApplication.getUser().getEmail().equals(username)) {
            return jobApplicationDTO;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public JobApplicationDTO getRecruiterJobApplication(UUID applicationId, String username) {
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
                .cvUrl(curriculumVitaeService.get(applicationId, username))
                .build();

        if (jobApplication.getJob().getUser().getEmail().equals(username)) {
            return jobApplicationDTO;
        } else {
            throw new AccessDeniedException("You are not authorized to access this resource");
        }
    }

    public JobApplication apply(UUID jobId, UUID cvId, String username) {
        User user = userService.getUserByEmail(username);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("Job not found"));
        Optional<JobApplication> existingJobApplication = jobApplicationRepository.findByJobAndUser(job,
                user);
        CurriculumVitae cv = curriculumVitaeService.find(cvId, user);

        if (existingJobApplication.isEmpty()) {
            JobApplication jobApplication = JobApplication.builder()
                    .status(JobApplicationStatus.PENDING)
                    .job(job)
                    .user(user)
                    .cv(cv)
                    .build();
            return jobApplicationRepository.save(jobApplication);
        } else if (existingJobApplication.isPresent()) {
            throw new IllegalArgumentException("You have already applied for this job");
        } else {
            throw new NoSuchElementException("Job not found");

        }
    }

    public JobApplication updateJobApplication(UUID applicationId, JobApplicationStatus status,
            List<EvaluationDTO> evaluations) {
        JobApplication jobApplication = getJobApplication(applicationId);
        if (evaluations.size() != jobApplication.getInterviewChatLogs().size()) {
            throw new IllegalArgumentException("Invalid number of evaluations");
        }
        jobApplication.setStatus(status);
        List<InterviewChatDTO> interviewChatLogs = jobApplication.getInterviewChatLogs();

        for (int i = 0; i < evaluations.size(); i++) {
            interviewChatLogs.get(i).setPredictedClass(evaluations.get(i).getPredictedClass());
            interviewChatLogs.get(i).setConfidence(evaluations.get(i).getConfidence());
            interviewChatLogs.get(i).setSecondaryModelPrediction(evaluations.get(i).getSecondaryModelPrediction());
            interviewChatLogs.get(i).setMainModelProbability(evaluations.get(i).getMainModelProbability());
        }
        return jobApplicationRepository.save(jobApplication);
    }

    public JobApplication save(JobApplication jobApplication) {
        return jobApplicationRepository.save(jobApplication);
    }
}
