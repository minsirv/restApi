CREATE TABLE national_id
(
    id            VARCHAR(255) NOT NULL,
    date_of_birth TIMESTAMP,
    gender        VARCHAR(255),
    CONSTRAINT pk_nationalid PRIMARY KEY (id)
);
CREATE TABLE validation_error
(
    id            VARCHAR(255) NOT NULL,
    error_message VARCHAR(255),
    national_id   VARCHAR(255),
    error_code    VARCHAR(255),
    CONSTRAINT pk_validationerror PRIMARY KEY (id)
);