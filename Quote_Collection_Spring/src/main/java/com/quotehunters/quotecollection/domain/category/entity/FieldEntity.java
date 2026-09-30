package com.quotehunters.quotecollection.domain.category.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "field")
public class FieldEntity {
    @Id
    @Column(name = "field_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "field_name", unique = true)
    private String name;

    public FieldEntity() {}

    public FieldEntity(int id, String name) {
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
        return "FieldEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
