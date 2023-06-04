INSERT INTO user(id, username, password, name, phone, role, tokens, registered_at, updated_at, is_enabled, is_non_locked)
VALUES ("bb3009a5-a30f-4498-a0e4-b693b287c5b3","user10","$2a$12$b5DPkdR7ik5nvkHm7CCNwu8gQSuMOEiewLwJxAWTssh0IwU9yRobe","user10","999",null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,false);
INSERT INTO user(id, username, password, name, phone, role, tokens, registered_at, updated_at, is_enabled, is_non_locked)
VALUES ("9e27e24d-4076-4151-b062-f3cc13c0ed74","user20","$2a$12$ONDQ20psCTXn5u3WkhTpZ.O87ihQKKVxwIGckEOM4vX1tzVqWKcFW","user20","888",null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,true);



INSERT INTO organisation(id, name, bullstat, user_id, address, website, facebook_link, charity_option, purpose_of_organisation, registered_at, updated_at)
VALUES ("1b1e18c4-2438-46ee-a6dc-6b83bf52a375","charity1","000123","bb3009a5-a30f-4498-a0e4-b693b287c5b3","Varna","wwwHTTp0","facebook link","tangibles","charity",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO organisation(id, name, bullstat, user_id, address, website, facebook_link, charity_option, purpose_of_organisation, registered_at, updated_at)
VALUES ("104dc378-4cfd-4db5-a34d-071dcdb02660","charity2","000245","9e27e24d-4076-4151-b062-f3cc13c0ed74","Sofia","wwwHTTp1","facebook link","taramtaram","live",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


-- Event records:
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES ("4bb893f1-572a-4806-8e54-0096a32e39b2","Sofia",CURRENT_TIMESTAMP,"abv32",CURRENT_TIMESTAMP,null,true,"event-1",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,"1b1e18c4-2438-46ee-a6dc-6b83bf52a375");
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES ("e8f2b9d3-6576-40c5-9e84-5ceed7ca84b6","Razgrad",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"event-2",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,"104dc378-4cfd-4db5-a34d-071dcdb02660");
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES ("52082edc-fab0-454c-adf2-a2f0c3c8441c","targovishte",CURRENT_TIMESTAMP,"abv-31",CURRENT_TIMESTAMP,null,true,"event-3",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,"104dc378-4cfd-4db5-a34d-071dcdb02660");