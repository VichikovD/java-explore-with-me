CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS statistics (
    statistic_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app VARCHAR NOT NULL,
    uri VARCHAR NOT NULL,
    ip VARCHAR NOT NULL,
    created TIMESTAMP NOT NULL
)