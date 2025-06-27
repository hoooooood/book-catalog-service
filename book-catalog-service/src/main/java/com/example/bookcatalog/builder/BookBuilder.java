package com.example.bookcatalog.builder;

import com.example.bookcatalog.entity.Book;

import java.math.BigDecimal;

public class BookBuilder {
    private String title;
    private String author;
    private String isbn;
    private Integer publicationYear;
    private BigDecimal price;

    public BookBuilder() {
    }

    public BookBuilder title(String title) {
        this.title = title;
        return this;
    }

    public BookBuilder author(String author) {
        this.author = author;
        return this;
    }

    public BookBuilder isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }

    public BookBuilder publicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
        return this;
    }

    public BookBuilder price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public BookBuilder price(double price) {
        this.price = BigDecimal.valueOf(price);
        return this;
    }

    public Book build() {
        Book book = new Book();
        book.setTitle(this.title);
        book.setAuthor(this.author);
        book.setIsbn(this.isbn);
        book.setPublicationYear(this.publicationYear);
        book.setPrice(this.price);
        return book;
    }

    public static BookBuilder builder() {
        return new BookBuilder();
    }

    public BookBuilder validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Author is required");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN is required");
        }
        return this;
    }
}

