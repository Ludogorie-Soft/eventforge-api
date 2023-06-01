INSERT INTO organisation (id, address, bullstat, categories, charity_option, name, phone, purpose_of_organisation, registered_at, updated_at, user_id)
VALUES (UUID(),"Varna","000123","Bussiness","tangibles","MODA","898989","charity",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);
INSERT INTO organisation (id, address, bullstat, categories, charity_option, name, phone, purpose_of_organisation, registered_at, updated_at, user_id)
VALUES (UUID(),"Targovishte","000123","Sport","something","live","898989","taramtaram",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);



-- Event records:
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES (10,"Sofia",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"namename",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES (11,"Razgrad",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"event",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);