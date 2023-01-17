CREATE TABLE job_details_v2
(
  id VARCHAR(50) PRIMARY KEY,
  correlation_id VARCHAR(50),
  status VARCHAR(40),
  last_update TIMESTAMPTZ,
  retries INT4,
  execution_counter INT4,
  scheduled_id VARCHAR(40),
  priority INT4,
  recipient JSONB,
  trigger JSONB,
  fire_time TIMESTAMPTZ
);

CREATE INDEX job_details_v2_fire_time_idx
    ON job_details_v2 (fire_time);