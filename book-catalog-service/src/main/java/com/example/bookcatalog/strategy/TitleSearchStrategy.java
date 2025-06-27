package com.example.bookcatalog.strategy;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TitleSearchStrategy implements SearchStrategy {
    
    @Override
    public List<Book> search(String searchTerm, BookRepository repository) {
        return repository.findByTitleContainingIgnoreCase(searchTerm);
    }
}

