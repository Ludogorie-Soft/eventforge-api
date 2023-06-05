INSERT INTO user(id, username, password, full_name, phone_number , role, tokens, registered_at, updated_at, is_enabled, is_non_locked)
VALUES (1,"user10","$2a$12$b5DPkdR7ik5nvkHm7CCNwu8gQSuMOEiewLwJxAWTssh0IwU9yRobe","user10","999",null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,false);
INSERT INTO user(id, username, password, full_name, phone_number , role, tokens, registered_at, updated_at, is_enabled, is_non_locked)
VALUES (2,"user20","$2a$12$ONDQ20psCTXn5u3WkhTpZ.O87ihQKKVxwIGckEOM4vX1tzVqWKcFW","user20","888",null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,true,true);



INSERT INTO organisation(id, name, bullstat, user_id, address, website, facebook_link, charity_option, organisation_purpose, registered_at, updated_at)
VALUES (1,"charity1","000123",1,"Varna","wwwHTTp0","facebook link","tangibles","charity",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO organisation(id, name, bullstat, user_id, address, website, facebook_link, charity_option, organisation_purpose, registered_at, updated_at)
VALUES (2,"charity2","000245",2,"Sofia","wwwHTTp1","facebook link","taramtaram","live",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


-- Event records:
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES (10,"Sofia",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"namename",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES (11,"Razgrad",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"event",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);