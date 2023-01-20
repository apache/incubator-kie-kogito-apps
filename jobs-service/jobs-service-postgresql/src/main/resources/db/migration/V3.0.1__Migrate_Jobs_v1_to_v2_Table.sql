INSERT INTO job_details (id, correlation_id, status, last_update, fire_time, retries, execution_counter, scheduled_id, priority, trigger, recipient)
    SELECT job.id as id,
           job.correlation_id as correlation_id,
           job.status as status,
           job.last_update as last_update,
           job.fire_time as fire_time,
           job.retries as retries,
           job.execution_counter as execution_counter,
           job.scheduled_id as scheduled_id,
           job.priority as priority,
           job.trigger as trigger,
           json_build_object('url', job.recipient ->> 'endpoint',
                             'type', 'http',
                             'method', 'POST',
                             'classType', 'org.kie.kogito.jobs.service.api.recipient.http.HttpRecipient',
                             'queryParams', '{}'::jsonb,
                             'headers','{}'::jsonb,
                             'payload', null
               ) as recipient
    FROM job_details_v1 job WHERE job.id is not null;