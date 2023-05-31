create table if not exists image(
    id BINARY(16) PRIMARY KEY,
    url VARCHAR(255),
    upload_at TIMESTAMP,
    update_at TIMESTAMP,
    type VARCHAR(255),
    event_id BINARY(16),
    organisation_id BINARY(16),
    FOREIGN KEY (event_id) REFERENCES event (id),
    FOREIGN KEY (organisation_id) REFERENCES organisation (id)
);
