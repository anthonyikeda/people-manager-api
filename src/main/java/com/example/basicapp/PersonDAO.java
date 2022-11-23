package com.example.basicapp;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "person")
public class PersonDAO {

    @Id
    @Column(name="person_id")
    @JsonProperty("person_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;

    private String name;

    private Integer age;

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
