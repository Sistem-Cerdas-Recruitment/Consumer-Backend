create table curriculum_vitae (
    id uuid not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    description varchar(255),
    file_name varchar(255),
    is_default boolean default false,
    original_file_name varchar(255),
    user_id uuid,
    primary key (id)
);

create table job (
    id uuid not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    additional_info varchar(255),
    advantages varchar(255) array,
    applicants integer,
    closed_at timestamp(6),
    description TEXT,
    experience_level varchar(255),
    interviewed integer,
    location varchar(255),
    majors varchar(255) array,
    mode varchar(255),
    offered_interview integer,
    requirements text array,
    responsibilities text array,
    salary varchar(255),
    skills text array,
    status smallint check (status between 0 and 1),
    title varchar(255),
    type varchar(255),
    years_of_experience integer,
    user_id uuid,
    primary key (id)
);

create table job_application (
    id uuid not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    experience jsonb,
    interview_chat_history jsonb,
    interview_score float4,
    is_relevant boolean,
    relevance_score float(53),
    status smallint check (status between 0 and 6),
    cv_id uuid,
    job_id uuid,
    user_id uuid,
    primary key (id)
);

create table users (
    id uuid not null,
    created_at timestamp(6),
    updated_at timestamp(6),
    email varchar(255),
    name varchar(255),
    password varchar(255),
    role varchar(255) check (
        role in (
            'ADMIN',
            'CANDIDATE',
            'RECRUITER'
        )
    ),
    primary key (id)
);

alter table if exists users drop constraint if exists unique_email;

alter table if exists users
add constraint unique_email unique (email);

alter table if exists curriculum_vitae
add constraint FKiitouosgqijxl80ibvejs6ol5 foreign key (user_id) references users on delete cascade;

alter table if exists job
add constraint FKpjwg3kcmu25r91o9x9nm6ha56 foreign key (user_id) references users;

alter table if exists job_application
add constraint FKpoom3qtl5ptiaoed8rg6seb8v foreign key (cv_id) references curriculum_vitae;

alter table if exists job_application
add constraint FKdepcvxeq3gyb4438ws0qjycc7 foreign key (job_id) references job;

alter table if exists job_application
add constraint FK7jtt0struc634qvwxhw43ipo8 foreign key (user_id) references users;