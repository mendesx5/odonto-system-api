CREATE TABLE clinical_records (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL REFERENCES patients(id),
    dentist_id UUID NOT NULL REFERENCES users(id),
    appointment_id UUID,
    record_date DATE NOT NULL,
    chief_complaint TEXT,
    diagnosis TEXT,
    treatment_plan TEXT,
    evolution_notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_records_patient ON clinical_records(patient_id);
CREATE INDEX idx_records_date ON clinical_records(record_date DESC);