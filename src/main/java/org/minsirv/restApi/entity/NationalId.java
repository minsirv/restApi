package org.minsirv.restApi.entity;

import org.minsirv.restApi.enumerators.Gender;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Entity
public class NationalId {

    @Id
    private String id;
    private Date dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @OneToMany(mappedBy = "nationalId", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ValidationError> validationErrors;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<ValidationError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<ValidationError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }
}
