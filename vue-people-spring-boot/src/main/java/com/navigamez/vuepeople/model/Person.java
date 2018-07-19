package com.navigamez.vuepeople.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Document
public class Person {

    private static final String DATE_REGEX =
            "(19|20)[0-9]{2}-((0[13578]|1[02])-(3[01]|0[1-9]|[12][0-9])|(0[469]|11)-(30|0[1-9]|[12][0-9])|02-(0[1-9]|[12][0-9]))";

    @Id
    private String id;
    @Size(min = 1)
    private String givenName;
    @Size(min = 1)
    private String familyName;
    @Pattern(regexp = DATE_REGEX)
    private String birthDate;
    private List<ContactInfo> contactInfos = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public List<ContactInfo> getContactInfos() {
        return contactInfos;
    }

    public void setContactInfos(List<ContactInfo> contactInfos) {
        this.contactInfos = contactInfos;
    }
}
