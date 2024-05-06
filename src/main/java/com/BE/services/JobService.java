package com.BE.services;

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
import org.springframework.stereotype.Service;

import com.BE.constants.Evaluation;
import com.BE.constants.JobApplicationStatus;
import com.BE.constants.JobStatus;
import com.BE.dto.job.JobApplicationResultDTO;
import com.BE.dto.job.JobResultDTO;
import com.BE.entities.CurriculumVitae;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.repositories.JobRepository;
import com.BE.repositories.projections.JobApplicationProjection;
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

    public List<JobResultDTO> findAllOpenJobs(String username) {
        User user = userService.getUserByEmail(username);
        List<JobProjection> jobs = jobRepository.findAllByStatus(JobStatus.OPEN, PageRequest.of(0, 10000, Sort.by(Sort.Direction.DESC, "updatedAt")));
        Map<UUID, JobApplicationProjection> jobApplications = jobApplicationRepository.findAllByUser(user).stream()
                .collect(Collectors.toMap(JobApplicationProjection::getJobId, Function.identity()));
        List<JobResultDTO> response = jobs.stream().map(job -> {
            JobResultDTO jobResponseDTO = new JobResultDTO();
            jobResponseDTO.setId(job.getId());
            jobResponseDTO.setTitle(job.getTitle());
            jobResponseDTO.setDescription(job.getDescription());
            jobResponseDTO.setUserId(job.getUser().getId());
            jobResponseDTO.setName(job.getUser().getName());
            jobResponseDTO.setApplied(jobApplications.containsKey(job.getId()));
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
                        JobApplicationResultDTO jobApplicationResultDTO = new JobApplicationResultDTO();
                        jobApplicationResultDTO.setId(jobApplication.getId());
                        jobApplicationResultDTO.setJobId(jobApplication.getJobId());
                        jobApplicationResultDTO.setJobTitle(jobApplication.getJob().getTitle());
                        jobApplicationResultDTO.setStatus(jobApplication.getStatus());
                        jobApplicationResultDTO.setUserId(jobApplication.getUser().getId());
                        jobApplicationResultDTO.setUserName(jobApplication.getUser().getName());
                        return jobApplicationResultDTO;
                    }).collect(Collectors.toList());
            return jobApplications;
        } else {
            throw new IllegalArgumentException("You are not authorized to access this resource");
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

    public JobApplication updateJobApplication(UUID applicationId, JobApplicationStatus status, Evaluation evaluation) {
        JobApplication jobApplication = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Job application not found"));
        jobApplication.setStatus(status);
        jobApplication.setEvaluation(evaluation);
        return jobApplicationRepository.save(jobApplication);
    }
}
