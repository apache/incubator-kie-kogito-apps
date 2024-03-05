ALTER TABLE Process_Instance_State_Log DROP CONSTRAINT Process_Instance_State_Log_event_type_check;
ALTER TABLE Process_Instance_State_Log ADD CONSTRAINT Process_Instance_State_Log_event_type_check CHECK (event_type IN ( 'ACTIVE','COMPLETED','SLA_VIOLATED', 'MIGRATED' ));