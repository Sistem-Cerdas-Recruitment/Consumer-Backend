package com.BE.repositories.projections;

import java.util.UUID;

import com.BE.constants.JobStatus;

public interface JobProjection {

    public UUID getId();

    public String getTitle();

    public String getDescription();

    public JobStatus getStatus();

    public UserIdName getUser();

    public interface UserIdName {
        public UUID getId();

        public String getName();
    }
}
