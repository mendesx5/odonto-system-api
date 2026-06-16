CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE patients (
    id UUID PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    cpf VARCHAR(14) NOT NULL UNIQUE,
    date_of_birth DATE NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(100),
    address VARCHAR(300),
    notes TEXT,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_patients_cpf ON patients(cpf);