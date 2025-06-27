package com.example.bookcatalog.strategy;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Component
public class IsbnSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Book> search(String searchTerm, BookRepository repository) {
        Optional<Book> book = repository.findByIsbn(searchTerm);
        return book.map(Collections::singletonList).orElse(Collections.emptyList());
    }
}

