package com.BE.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.constants.Evaluation;
import com.BE.constants.JobApplicationStatus;
import com.BE.constants.JobStatus;
import com.BE.entities.CurriculumVitae;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.repositories.JobRepository;

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

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    public List<JobApplication> findApplications(UUID jobId, String username) {
        User user = userService.getUserByEmail(username);
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NoSuchElementException("Job not found"));
        if (job.getUser().getId().equals(user.getId())) {
            return jobApplicationRepository.findByJob(job);
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
