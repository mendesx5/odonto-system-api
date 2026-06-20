CREATE TABLE exam_files (
    id UUID PRIMARY KEY,
    patient_id UUID NOT NULL REFERENCES patients(id),
    clinical_record_id UUID REFERENCES clinical_records(id),
    original_name VARCHAR(255) NOT NULL,
    storage_key VARCHAR(500) NOT NULL,
    public_url VARCHAR(500) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    file_size_bytes BIGINT NOT NULL,
    exam_type VARCHAR(50) NOT NULL DEFAULT 'OUTRO',
    description TEXT,
    exam_date DATE,
    uploaded_by UUID NOT NULL REFERENCES users(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_exam_files_patient ON exam_files(patient_id);
CREATE INDEX idx_exam_files_record  ON exam_files(clinical_record_id);