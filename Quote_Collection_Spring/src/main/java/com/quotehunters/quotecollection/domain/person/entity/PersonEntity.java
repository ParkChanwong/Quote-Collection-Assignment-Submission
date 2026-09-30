package com.quotehunters.quotecollection.domain.person.entity;

import com.quotehunters.quotecollection.domain.category.entity.CountryEntity;
import com.quotehunters.quotecollection.domain.category.entity.FieldEntity;
import com.quotehunters.quotecollection.domain.category.entity.PeriodEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "person")
public class PersonEntity {
    @Id
    @Column(name = "person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne
    @JoinColumn(name = "period_id")
    private PeriodEntity period;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private FieldEntity field;

    @Column(name = "person_name")
    private String name;

    public PersonEntity() {}

    public PersonEntity(int id, CountryEntity country, PeriodEntity period, FieldEntity field, String name) {
        this.id = id;
        this.country = country;
        this.period = period;
        this.field = field;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public PeriodEntity getPeriod() {
        return period;
    }

    public void setPeriod(PeriodEntity period) {
        this.period = period;
    }

    public FieldEntity getField() {
        return field;
    }

    public void setField(FieldEntity field) {
        this.field = field;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PersonEntity{" +
                "id=" + id +
                ", country=" + country +
                ", period=" + period +
                ", field=" + field +
                ", name='" + name + '\'' +
                '}';
    }
}
