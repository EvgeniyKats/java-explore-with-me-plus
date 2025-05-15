CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL UNIQUE,
    );

CREATE TABLE IF NOT EXISTS categories (
    category_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    category_name VARCHAR(50) NOT NULL UNIQUE,
    );

CREATE TABLE IF NOT EXISTS locations (
    location_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    latitude NUMERIC(9, 6) NOT NULL,
    longitude NUMERIC(9, 6) NOT NULL,
    );

CREATE TABLE IF NOT EXISTS events (
    event_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_title VARCHAR(155) NOT NULL,
    description VARCHAR(3000) NOT NULL,
    annotation VARCHAR(500) NOT NULL,
    initiator_id FOREIGN KEY REFERENCES users(user_id),
    category_id FOREIGN KEY REFERENCES categories (category_id),
    location_id FOREIGN KEY REFERENCES locations (location_id),
    views BIGINT,
    state VARCHAR(50) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    event_date TIMESTAMP WITHOUT TIME ZONE,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    paid BOOLEAN NOT NULL,
    confirmed_requests BIGINT,
    participant_limit INTEGER,
    request_moderation BOOLEAN
    );

CREATE TABLE IF NOT EXISTS requests (
    request_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    requester_id BIGINT NOT NULL FOREIGN KEY REFERENCES users (user_id),
    event_id BIGINT NOT NULL FOREIGN KEY REFERENCES events (event_id),
    status VARCHAR(120) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE
    );

CREATE TABLE IF NOT EXISTS compilations (
    compilation_id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NULL,
    compilation_title VARCHAR(50) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS events_compilations (
    event_id BIGINT REFERENCES events (event_id) ON DELETE CASCADE,
    compilation_id BIGINT REFERENCES compilations (compilation_id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, compilation_id)
    );














