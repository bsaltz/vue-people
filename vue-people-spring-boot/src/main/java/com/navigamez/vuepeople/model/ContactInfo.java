package com.navigamez.vuepeople.model;

import io.micrometer.core.lang.NonNull;

import javax.validation.constraints.Size;

public class ContactInfo {

    @NonNull
    private ContactInfoType type;
    @Size(min = 1)
    private String data;
    private String customLabel;

    public ContactInfoType getType() {
        return type;
    }

    public void setType(ContactInfoType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(String customLabel) {
        this.customLabel = customLabel;
    }
}
