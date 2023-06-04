create table if not exists user(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) ,
    password VARCHAR(255),
    full_name VARCHAR(255),
    phone_number VARCHAR(255),
    role VARCHAR(255),
    tokens VARCHAR(255),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_enabled BOOLEAN,
    is_non_locked BOOLEAN
    );
