create table if not exists event(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    description VARCHAR(255),
    address VARCHAR(255),
    event_categories VARBINARY(255),
    organisation_id BIGINT,
    is_online BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    starts_at TIMESTAMP,
    ends_at TIMESTAMP,
    FOREIGN KEY (organisation_id) REFERENCES organisation (id)
);