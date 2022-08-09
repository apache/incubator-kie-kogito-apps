ALTER TABLE job_details
    ADD COLUMN fire_time INT8;

CREATE INDEX job_details_fire_time_idx
    ON job_details (fire_time);
