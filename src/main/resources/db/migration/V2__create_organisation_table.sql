create table if not exists organisation(
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255),
    bullstat VARCHAR(255),
    user_id BINARY(16),
    phone VARCHAR(255),
    address VARCHAR(255),
    charity_option VARCHAR(255),
    purpose_of_organisation VARCHAR(255),
    categories VARBINARY(255),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user (id)
);