package com.BE.repositories.projections;

import java.util.UUID;

import com.BE.constants.JobApplicationStatus;

public interface JobApplicationProjection {

    interface JobApplicationUserJobProjection {
        public UUID getId();

        public JobApplicationStatus getStatus();

        public UserIdName getUser();

        public JobId getJob();

        default UUID getJobId() {
            return getJob().getId();
        }

        interface JobId {
            public UUID getId();

            public String getTitle();

            public UserIdName getUser();
        }

        interface UserIdName {
            public UUID getId();

            public String getName();
        }

    }

}
