INSERT INTO organisation(id, name, bullstat, user_id, address, website, facebook_link, charity_option, purpose_of_organisation, registered_at, updated_at)
VALUES (UUID(),"charity","000123",null,"Varna","wwwHTTp0","facebook link","tangibles","charity",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
INSERT INTO organisation(id, name, bullstat, user_id, address, website, facebook_link, charity_option, purpose_of_organisation, registered_at, updated_at)
VALUES (UUID(),"charity","000245",null,"Sofia","wwwHTTp1","facebook link","taramtaram","live",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);


-- Event records:
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES (10,"Sofia",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"namename",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);
INSERT INTO event (id, address, created_at, description, ends_at, event_categories, is_online, name, starts_at, updated_at, organisation_id)
VALUES (11,"Razgrad",CURRENT_TIMESTAMP,"event info",CURRENT_TIMESTAMP,null,true,"event",CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,null);