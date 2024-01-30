DROP SCHEMA IF EXISTS public CASCADE;
CREATE SCHEMA IF NOT EXISTS public;

CREATE TABLE IF NOT EXISTS locations (
    location_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL UNIQUE
--  ON DELETE RESTRICT "Обратите внимание: с категорией не должно быть связано ни одного события."
--  example:
--{
--  "status": "CONFLICT",
--  "reason": "For the requested operation the conditions are not met.",
--  "message": "The category is not empty",
--  "timestamp": "2023-01-21 16:56:19"
--}
);

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation VARCHAR NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories (category_id) ON DELETE RESTRICT,
    created_on TIMESTAMP NOT NULL,
    description VARCHAR NOT NULL,
    event_date TIMESTAMP NOT NULL,
    initiator_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    location_id BIGINT NOT NULL REFERENCES locations (location_id) ON DELETE RESTRICT,
    paid BOOLEAN NOT NULL,
    participant_limit INTEGER NOT NULL,
    published_on TIMESTAMP,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR NOT NULL,
    title VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS event_requests (
    event_request_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES events (event_id) ON DELETE RESTRICT,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE RESTRICT,
    status VARCHAR NOT NULL,
    created TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL REFERENCES compilations (compilation_id),
    event_id BIGINT NOT NULL REFERENCES events (event_id),
    PRIMARY KEY(event_id, compilation_id)
);