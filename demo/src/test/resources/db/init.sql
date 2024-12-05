CREATE SCHEMA IF NOT EXISTS example;
DROP TABLE IF EXISTS service_timestamp CASCADE;
CREATE TABLE service_timestamp
(
    id        SERIAL PRIMARY KEY,
    timestamp TIMESTAMP NOT NULL
);