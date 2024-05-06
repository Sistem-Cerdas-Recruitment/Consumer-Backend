package com.BE.repositories.projections;

import java.util.UUID;

public interface JobProjection {

    public UUID getId();

    public String getTitle();

    public String getDescription();

    public JobStatus getStatus();

    public UserIdName getUser();

    public String getName();

    public Boolean getApplied();

    public interface JobStatus {
        public String name();
    }

    public interface UserIdName {
        public UUID getId();

        public String getName();
    }
}
