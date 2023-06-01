create table if not exists user(
    id BINARY(16) PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    name VARCHAR(255),
    phone VARCHAR(255),
    role VARCHAR(255),
    tokens VARCHAR(255),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    is_enabled BOOLEAN,
    is_non_locked BOOLEAN
    );
