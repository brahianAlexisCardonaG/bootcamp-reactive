CREATE TABLE bootcamp (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    release_date DATE NOT NULL,
    duration INT NOT NULL
);

INSERT INTO bootcamp (name, release_date, duration) VALUES
('front TypeScript', '31/05/2025', 5 );