package com.example.bookcatalog.validator;

import com.example.bookcatalog.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class StandardBookValidator implements BookValidator {
    
    @Override
    public void validate(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be null or empty");
        }
        
        if (book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN cannot be null or empty");
        }
        
        if (!isValidIsbn(book.getIsbn())) {
            throw new IllegalArgumentException("Invalid ISBN format");
        }
        
        if (book.getPublicationYear() != null && 
            (book.getPublicationYear() < 1000 || book.getPublicationYear() > 2030)) {
            throw new IllegalArgumentException("Publication year must be between 1000 and 2030");
        }
        
        if (book.getPrice() != null && book.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Book price cannot be negative");
        }
    }
    
    private boolean isValidIsbn(String isbn) {
        String cleanIsbn = isbn.replaceAll("-", "");
        return cleanIsbn.length() == 10 || cleanIsbn.length() == 13;
    }
}

