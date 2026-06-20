CREATE TABLE record_procedures (
    clinical_record_id UUID NOT NULL REFERENCES clinical_records(id),
    procedure_id UUID NOT NULL REFERENCES procedure_catalog(id),
    PRIMARY KEY (clinical_record_id, procedure_id)
);
