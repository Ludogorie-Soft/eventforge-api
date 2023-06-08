create table if not exists event_enrollment(
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  phone VARCHAR(255),
                                  external_link VARCHAR(255),
                                  email VARCHAR(255),
                                  event_id BIGINT,
                                  FOREIGN KEY (event_id) REFERENCES event (id)
);
