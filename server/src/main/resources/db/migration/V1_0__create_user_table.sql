CREATE TABLE IF NOT EXISTS "user"(
    id SERIAL PRIMARY KEY,
    username VARCHAR(26) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    registration_date TIMESTAMP NOT NULL,
    last_working_date TIMESTAMP NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    birth_date DATE NOT NULL);

CREATE TABLE IF NOT EXISTS role(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL);