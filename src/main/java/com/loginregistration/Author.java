package com.loginregistration;

public class Author {
    private String name;
    private String surname;
    private int age;
    private int numberOfBooks;

    public Author(String name, String surname, int age, int numberOfBooks) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.numberOfBooks = numberOfBooks;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getNumberOfBooks() {
        return numberOfBooks;
    }

    public void setNumberOfBooks(int numberOfBooks) {
        this.numberOfBooks = numberOfBooks;
    }
}
