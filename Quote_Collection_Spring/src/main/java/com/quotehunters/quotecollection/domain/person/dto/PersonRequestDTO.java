package com.quotehunters.quotecollection.domain.person.dto;

public class PersonRequestDTO {
    private int countryId;
    private int periodId;
    private int fieldId;
    private String name;

    public PersonRequestDTO() {}

    public PersonRequestDTO(int countryId, int periodId, int fieldId, String name) {
        this.countryId = countryId;
        this.periodId = periodId;
        this.fieldId = fieldId;
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getPeriodId() {
        return periodId;
    }

    public void setPeriodId(int periodId) {
        this.periodId = periodId;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonRequestDTO{" +
                "countryId=" + countryId +
                ", periodId=" + periodId +
                ", fieldId=" + fieldId +
                ", name='" + name + '\'' +
                '}';
    }
}
