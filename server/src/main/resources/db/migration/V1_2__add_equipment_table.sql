CREATE TABLE IF NOT EXISTS technological_equimpent(
    id SERIAL PRIMARY KEY,
    equipment_index VARCHAR(20) NOT NULL UNIQUE,
    image_path VARCHAR(255) NOT NULL UNIQUE,
    note VARCHAR(255),
    added_date TIMESTAMP NOT NULL DEFAULT now()
)