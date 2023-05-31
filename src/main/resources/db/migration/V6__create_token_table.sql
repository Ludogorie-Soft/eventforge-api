CREATE TABLE IF NOT EXISTS token (
                                      id BINARY(16) PRIMARY KEY,
                                      token VARCHAR(255) UNIQUE,
                                      token_type VARCHAR(50),
                                      revoked BOOLEAN,
                                      expired BOOLEAN,
                                      user_id BINARY(16),
                                      FOREIGN KEY (user_id) REFERENCES user(id)
);