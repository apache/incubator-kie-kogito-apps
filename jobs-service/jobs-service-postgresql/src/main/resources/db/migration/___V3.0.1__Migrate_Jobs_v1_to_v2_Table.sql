INSERT INTO new_player_data (player_name, gender, country, weight_kg, height_cm)
SELECT player_name, gender, country, weight_kg, height_cm
FROM player_data
WHERE weight_kg > 50;

INSERT INTO table_b (col1, col2, col3, col4, col5, col6)
SELECT col1, 'str_val', int_val, col4, col5, col6
FROM table_a

SELECT job.recipient ->> 'url' AS u,  job.recipient ->> 'headers' as head
FROM job_details_v2 job WHERE job.id is not null;


SELECT job.recipient ->> 'endpoint' AS url,
    job.id,
    job.correlation_id,
    job.status,
    job.last_update,
    job.retries,
    job.execution_counter,
    job.scheduled_id,
    job.priority,
    job.trigger
FROM job_details job WHERE job.id is not null;

INSERT INTO job_details_v2 (id, correlation_id, status, last_update,retries, priority, execution_counter, scheduled_id, recipient, trigger)
