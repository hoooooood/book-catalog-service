package com.example.bookcatalog.validator;

import com.example.bookcatalog.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class StandardBookValidatorTest {

    private StandardBookValidator validator;
    private Book validBook;

    @BeforeEach
    void setUp() {
        validator = new StandardBookValidator();
        validBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
    }

    @Test
    void validate_WithValidBook_ShouldNotThrowException() {
        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }

    @Test
    void validate_WithNullBook_ShouldThrowException() {
        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(null)
        );
        assertEquals("Book cannot be null", exception.getMessage());
    }

    @Test
    void validate_WithNullTitle_ShouldThrowException() {
        
        validBook.setTitle(null);

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Book title cannot be null or empty", exception.getMessage());
    }

    @Test
    void validate_WithEmptyTitle_ShouldThrowException() {
        
        validBook.setTitle("   ");

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Book title cannot be null or empty", exception.getMessage());
    }

    @Test
    void validate_WithNullAuthor_ShouldThrowException() {
        
        validBook.setAuthor(null);

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Book author cannot be null or empty", exception.getMessage());
    }

    @Test
    void validate_WithEmptyAuthor_ShouldThrowException() {
        
        validBook.setAuthor("");

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Book author cannot be null or empty", exception.getMessage());
    }

    @Test
    void validate_WithNullIsbn_ShouldThrowException() {
        
        validBook.setIsbn(null);

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Book ISBN cannot be null or empty", exception.getMessage());
    }

    @Test
    void validate_WithInvalidIsbnLength_ShouldThrowException() {
        
        validBook.setIsbn("123");

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Invalid ISBN format", exception.getMessage());
    }

    @Test
    void validate_WithValidIsbn10_ShouldNotThrowException() {
        
        validBook.setIsbn("1234567890");

        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }

    @Test
    void validate_WithValidIsbn13_ShouldNotThrowException() {
        
        validBook.setIsbn("1234567890123");

        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }

    @Test
    void validate_WithIsbnWithHyphens_ShouldNotThrowException() {
        
        validBook.setIsbn("123-456-789-0");

        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }

    @Test
    void validate_WithInvalidPublicationYearTooLow_ShouldThrowException() {
        
        validBook.setPublicationYear(999);

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Publication year must be between 1000 and 2030", exception.getMessage());
    }

    @Test
    void validate_WithInvalidPublicationYearTooHigh_ShouldThrowException() {
        
        validBook.setPublicationYear(2031);

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Publication year must be between 1000 and 2030", exception.getMessage());
    }

    @Test
    void validate_WithNullPublicationYear_ShouldNotThrowException() {
        
        validBook.setPublicationYear(null);

        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }

    @Test
    void validate_WithNegativePrice_ShouldThrowException() {
        
        validBook.setPrice(new BigDecimal("-10.00"));

        
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> validator.validate(validBook)
        );
        assertEquals("Book price cannot be negative", exception.getMessage());
    }

    @Test
    void validate_WithNullPrice_ShouldNotThrowException() {
        
        validBook.setPrice(null);

        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }

    @Test
    void validate_WithZeroPrice_ShouldNotThrowException() {
        
        validBook.setPrice(BigDecimal.ZERO);

        
        assertDoesNotThrow(() -> validator.validate(validBook));
    }
}

