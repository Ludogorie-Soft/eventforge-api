create table if not exists organisation(
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255),
    bullstat VARCHAR(255),
    user_id BINARY(16),
    address VARCHAR(255),
    website VARCHAR(255),
    facebook_link VARCHAR(255),
    charity_option VARCHAR(255),
    purpose_of_organisation VARCHAR(255),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
);

create table if not exists organisation_priority (
                                       id BINARY(16) PRIMARY KEY,
                                       category VARCHAR(255) NOT NULL
);

create table if not exists priority_id_organisation_id (
                                                           organisation_id BINARY(16),
                                                           organisation_priority_id BINARY(16),
                                                           priority_id BINARY(16),
                                                           FOREIGN KEY (organisation_id) REFERENCES organisation(id),
                                                           FOREIGN KEY (organisation_priority_id) REFERENCES organisation_priority(id),
                                                           PRIMARY KEY (organisation_id, organisation_priority_id)
);
