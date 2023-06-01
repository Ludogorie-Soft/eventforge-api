create table if not exists event(
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255),
    description VARCHAR(255),
    address VARCHAR(255),
    event_categories VARBINARY(255),
    organisation_id BINARY(16),
    is_online BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    FOREIGN KEY (organisation_id) REFERENCES organisation (id)
);