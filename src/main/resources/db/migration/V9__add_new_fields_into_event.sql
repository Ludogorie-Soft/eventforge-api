ALTER TABLE event
    ADD price DOUBLE(10,2) DEFAULT 0.0,
    ADD min_age INTEGER,
    ADD max_age INTEGER;