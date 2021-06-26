package com.loginregistration;

import org.mindrot.jbcrypt.BCrypt;

public class Person {
    private String name;
    private String surname;
    private String email;
    private String username;
    private String password;
    private String salt = BCrypt.gensalt();
    private String role;

    public Person(String name, String surname, String email, String username, String password, String role) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.username = username;
        this.password = BCrypt.hashpw(password, salt);
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, salt);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
