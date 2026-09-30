package com.quotehunters.quotecollection.domain.person.dto;

public class PersonResponseDTO {
    private int id;
    private String countryName;
    private String periodName;
    private String fieldName;
    private String name;

    public PersonResponseDTO() {}

    public PersonResponseDTO(int id, String countryName, String periodName, String fieldName, String name) {
        this.id = id;
        this.countryName = countryName;
        this.periodName = periodName;
        this.fieldName = fieldName;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonResponseDTO{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", periodName='" + periodName + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
