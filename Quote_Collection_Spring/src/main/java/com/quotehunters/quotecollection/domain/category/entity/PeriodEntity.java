package com.quotehunters.quotecollection.domain.category.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "period")
public class PeriodEntity {
    @Id
    @Column(name = "period_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "period_name", unique = true)
    private String name;

    public PeriodEntity() {}

    public PeriodEntity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PeriodEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
