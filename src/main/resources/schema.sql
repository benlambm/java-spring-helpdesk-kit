-- Spring Boot runs this file on every start. IF NOT EXISTS makes that safe.
CREATE TABLE IF NOT EXISTS tickets (
    id         VARCHAR(20) PRIMARY KEY,
    category   VARCHAR(60) NOT NULL,
    priority   VARCHAR(10) NOT NULL,
    status     VARCHAR(10) NOT NULL,
    technician VARCHAR(40) NOT NULL
);
