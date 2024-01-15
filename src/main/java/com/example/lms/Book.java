
package com.example.lms;

import java.util.Date;


public class Book {
    private String author;
    private String title;
    private String category;
    private int book_id;
    private Date return_date;


    public Book(String author, String title, String category, int book_id) {
        this.book_id = book_id;
        this.author = author;
        this.title = title;
        this.category = category;
    }


    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getReturn_date() {
        return return_date;
    }

    public void setReturn_date(Date return_date) {
        this.return_date = return_date;
    }
}


