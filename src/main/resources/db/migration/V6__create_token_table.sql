CREATE TABLE IF NOT EXISTS token (
                                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                      token_value VARCHAR(255) UNIQUE,
                                      token_type VARCHAR(50),
                                      revoked BOOLEAN,
                                      expired BOOLEAN,
                                      user_id BIGINT,
                                      FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE IF NOT EXISTS verification_token (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    token VARCHAR(255) NOT NULL,
                                    user_id BIGINT,
                                    expiration_time TIMESTAMP,
                                    FOREIGN KEY (user_id) REFERENCES user(id)
);