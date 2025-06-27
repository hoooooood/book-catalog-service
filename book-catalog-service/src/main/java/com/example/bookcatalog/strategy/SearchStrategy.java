package com.example.bookcatalog.strategy;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;

import java.util.List;

public interface SearchStrategy {
    List<Book> search(String searchTerm, BookRepository repository);
}

