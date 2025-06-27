package com.example.bookcatalog.validator;

import com.example.bookcatalog.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UpdateBookValidatorTest {

    private UpdateBookValidator validator;
    private Book validBook;

    @BeforeEach
    void setUp() {
        validator = new UpdateBookValidator();
        validBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
    }

    @Test
    void validate_WithValidBook_ShouldNotThrowException() {
        assertDoesNotThrow(() -> validator.validate(validBook));
    }
}
