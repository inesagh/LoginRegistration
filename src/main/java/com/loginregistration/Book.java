package com.loginregistration;

public class Book {
    private String title;
    private String publishedYear;

    public Book(String title, String publishedYear) {
        this.title = title;
        this.publishedYear = publishedYear;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishedYear() {
        return publishedYear;
    }

    public void setPublishedYear(String publishedYear) {
        this.publishedYear = publishedYear;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", publishedYear='" + publishedYear + '\'' +
                '}';
    }
}
