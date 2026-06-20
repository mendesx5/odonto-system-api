CREATE TABLE procedure_catalog (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    category VARCHAR(100) NOT NULL,
    default_duration_minutes INTEGER,
    description TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);