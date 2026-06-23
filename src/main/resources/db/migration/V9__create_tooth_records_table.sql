CREATE TABLE tooth_records (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL REFERENCES patients(id),
    clinical_record_id UUID REFERENCES clinical_records(id),
    dentist_id UUID NOT NULL REFERENCES users(id),
    tooth_number INTEGER NOT NULL CHECK (tooth_number BETWEEN 11 AND 85),
    status VARCHAR(30) NOT NULL DEFAULT 'HIGIDO',
    notes TEXT,
    recorded_at DATE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_tooth_patient ON tooth_records(patient_id, tooth_number);
CREATE INDEX idx_tooth_record  ON tooth_records(clinical_record_id);