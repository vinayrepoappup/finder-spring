package com.example.finderspring.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String id;
    String firstName;
    String lastName;
    String email;
    String domain;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDomain() {
        return domain;
    }

    @Override
    public String toString() {
        return "Email{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", domain='" + domain + '\'' +
                '}';
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Email(String id, String firstName, String lastName, String email, String domain) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.domain = domain;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



}
