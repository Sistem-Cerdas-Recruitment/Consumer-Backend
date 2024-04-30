package com.BE.services;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BE.entities.CurriculumVitae;
import com.BE.entities.Job;
import com.BE.entities.JobApplication;
import com.BE.entities.User;
import com.BE.repositories.JobApplicationRepository;
import com.BE.repositories.JobRepository;

@Service
public class JobService {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    JobApplicationRepository jobApplicationRepository;

    @Autowired
    CurriculumVitaeService curriculumVitaeService;

    public Job find(UUID id) {
        return jobRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Job not found"));
    }

    public Job createJob(String title, String description, User user) {
        Job job = Job.builder()
                .title(title)
                .description(description)
                .user(user)
                .build();
        return jobRepository.save(job);
    }

    public JobApplication apply(UUID jobId, UUID cvId, User user) {
        Optional<Job> job = jobRepository.findById(jobId);
        Optional<JobApplication> existingJobApplication = jobApplicationRepository.findByJobIdAndUserId(jobId,
                user.getId());
        CurriculumVitae cv = curriculumVitaeService.find(cvId, user);

        if (existingJobApplication.isEmpty() && job.isPresent()) {
            JobApplication jobApplication = JobApplication.builder()
                    .job(job.get())
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
}
