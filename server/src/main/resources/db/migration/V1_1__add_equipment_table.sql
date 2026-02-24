CREATE TABLE IF NOT EXISTS technological_equipment(
    id SERIAL PRIMARY KEY,
    equipment_index VARCHAR(20) NOT NULL UNIQUE,
    image_path VARCHAR(255) NOT NULL UNIQUE,
    note VARCHAR(255)
)