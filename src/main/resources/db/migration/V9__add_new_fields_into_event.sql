ALTER TABLE event
    ADD price DOUBLE(10,2) DEFAULT 0.0,
    ADD min_age INTEGER DEFAULT 0,
    ADD max_age INTEGER DEFAULT 0 ,
    ADD recurrence_details VARCHAR(255) DEFAULT NULL;