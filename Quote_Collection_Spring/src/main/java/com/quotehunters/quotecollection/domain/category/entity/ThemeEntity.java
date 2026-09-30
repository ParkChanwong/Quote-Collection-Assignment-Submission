package com.quotehunters.quotecollection.domain.category.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "theme")
public class ThemeEntity {
    @Id
    @Column(name = "theme_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "theme_name", unique = true)
    private String name;

    public ThemeEntity() {}

    public ThemeEntity(int id, String name) {
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
        return "ThemeEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
