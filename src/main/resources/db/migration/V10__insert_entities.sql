INSERT INTO `user`(`id`, `username`, `password`, `full_name`, `phone_number`, `role`, `tokens`, `registered_at`, `updated_at`, `is_enabled`, `is_non_locked`, `is_approved_by_admin`)
VALUES (2,'proba@abv.bg','$2a$12$cFE5E8/GbarRXQs2L.Qw3eW1iG4Bxf2SARsdsFkwoYI69lb/cfnhK','Proba Proba','123123123123','ORGANISATION',null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,true,true);

INSERT INTO `user`(`id`, `username`, `password`, `full_name`, `phone_number`, `role`, `tokens`, `registered_at`, `updated_at`, `is_enabled`, `is_non_locked`, `is_approved_by_admin`)
VALUES (3,'proba1@abv.bg','$2a$12$u0SVotMf1Sx6YWrTVexaJ.tSRJFvit9jjs9icQ1cvwLx7YUNrf9iG','Proba1 Proba1','0000000000','ORGANISATION',null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,true,true);

INSERT INTO `user`(`id`, `username`, `password`, `full_name`, `phone_number`, `role`, `tokens`, `registered_at`, `updated_at`, `is_enabled`, `is_non_locked`, `is_approved_by_admin`)
VALUES (4,'proba2@abv.bg','$2a$12$6vE0SDT39sDqADqqsNwLR.vOu4au.WNvhsX63TIwRKD9XaVTiZv8G','Proba2 Proba2','111111111','ORGANISATION',null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,true,true);

INSERT INTO `organisation`(`id`, `name`, `bullstat`, `user_id`, `address`, `website`, `facebook_link`, `charity_option`, `organisation_purpose`, `registered_at`, `updated_at`)
VALUES (1,'Proba-org','123123123',2,'adressss',null,null,null,'aaaaaaaaaaaaaa',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO `organisation`(`id`, `name`, `bullstat`, `user_id`, `address`, `website`, `facebook_link`, `charity_option`, `organisation_purpose`, `registered_at`, `updated_at`)
VALUES (2,'Proba-org1','123123123',3,'adressss1',null,null,null,'aaaaaaaaaaaaaa1',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO `organisation`(`id`, `name`, `bullstat`, `user_id`, `address`, `website`, `facebook_link`, `charity_option`, `organisation_purpose`, `registered_at`, `updated_at`)
VALUES (3,'Proba-org2','123123123',4,'adressss2',null,null,null,'aaaaaaaaaaaaaa2',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO `event`(`id`, `name`, `description`, `address`, `event_categories`, `organisation_id`, `is_online`, `created_at`, `updated_at`, `starts_at`, `ends_at`, `is_one_time`, `price`, `min_age`, `max_age`, `recurrence_details`)
VALUES (1,'Event1','event1','Sofia-grad','aaaaaaaaa',2,true,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'2023-06-15 ','2023-06-16 ',true,2.50,12,16,null)
