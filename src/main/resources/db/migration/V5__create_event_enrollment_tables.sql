CREATE TABLE event_enrollment (
                                  id BINARY(16) PRIMARY KEY,
                                  phone VARCHAR(255),
                                  external_link VARCHAR(255),
                                  email VARCHAR(255)
);

CREATE TABLE event_enrollment_event (
                                        event_enrollment_id BINARY(16),
                                        event_id BINARY(16),
                                        PRIMARY KEY (event_enrollment_id, event_id),
                                        FOREIGN KEY (event_enrollment_id) REFERENCES event_enrollment (id),
                                        FOREIGN KEY (event_id) REFERENCES event (id)
);