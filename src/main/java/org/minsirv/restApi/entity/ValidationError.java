package org.minsirv.restApi.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ValidationError {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String errorMessage;
    private String nationalId;
    private String errorCode;

    public ValidationError(String nationalId, String errorMessage, String errorCode) {
        this.nationalId = nationalId;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
    }

    public ValidationError() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
}
