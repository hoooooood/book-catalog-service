package com.example.bookcatalog.decorator;

import com.example.bookcatalog.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookServiceDecorator {
    List<Book> getAllBooks();
    Optional<Book> getBookById(Long id);
    Optional<Book> getBookByIsbn(String isbn);
    List<Book> getBooksByAuthor(String author);
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooks(String searchType, String searchTerm);
    Book saveBookWithBuilder(String title, String author, String isbn, Integer publicationYear, Double price);
    Book updateBook(Long id, Book bookDetails);
    void deleteBook(Long id);
}

