create table if not exists contact(
                                  id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                  email VARCHAR(255),
                                  name VARCHAR(255),
                                  subject VARCHAR(255),
                                  text TEXT,
                                  is_answered BOOLEAN DEFAULT FALSE,
                                  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
