package com.example.bookcatalog.builder;

import com.example.bookcatalog.entity.Book;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BookBuilderTest {

    @Test
    void builder_WithAllFields_ShouldCreateBookCorrectly() {
        String title = "Test Title";
        String author = "Test Author";
        String isbn = "1234567890";
        Integer publicationYear = 2023;
        BigDecimal price = new BigDecimal("29.99");
        
        Book book = BookBuilder.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .publicationYear(publicationYear)
                .price(price)
                .build();

         
        assertNotNull(book);
        assertEquals(title, book.getTitle());
        assertEquals(author, book.getAuthor());
        assertEquals(isbn, book.getIsbn());
        assertEquals(publicationYear, book.getPublicationYear());
        assertEquals(price, book.getPrice());
    }

    @Test
    void builder_WithPriceAsDouble_ShouldConvertToBigDecimal() {
           
        double priceValue = 39.99;

          
        Book book = BookBuilder.builder()
                .title("Test Title")
                .author("Test Author")
                .isbn("1234567890")
                .price(priceValue)
                .build();

         
        assertNotNull(book.getPrice());
        assertEquals(BigDecimal.valueOf(priceValue), book.getPrice());
    }

    @Test
    void builder_WithOptionalFields_ShouldCreateBookWithNulls() {
          
        Book book = BookBuilder.builder()
                .title("Test Title")
                .author("Test Author")
                .isbn("1234567890")
                .build();

         
        assertNotNull(book);
        assertEquals("Test Title", book.getTitle());
        assertEquals("Test Author", book.getAuthor());
        assertEquals("1234567890", book.getIsbn());
        assertNull(book.getPublicationYear());
        assertNull(book.getPrice());
    }

    @Test
    void builder_WithValidation_ShouldPassValidation() {
               
        assertDoesNotThrow(() -> {
            BookBuilder.builder()
                    .title("Test Title")
                    .author("Test Author")
                    .isbn("1234567890")
                    .validate()
                    .build();
        });
    }

    @Test
    void builder_WithEmptyTitle_ShouldFailValidation() {
               
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            BookBuilder.builder()
                    .title("")
                    .author("Test Author")
                    .isbn("1234567890")
                    .validate()
                    .build();
        });
        assertEquals("Title is required", exception.getMessage());
    }

    @Test
    void builder_WithNullAuthor_ShouldFailValidation() {
               
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            BookBuilder.builder()
                    .title("Test Title")
                    .author(null)
                    .isbn("1234567890")
                    .validate()
                    .build();
        });
        assertEquals("Author is required", exception.getMessage());
    }

    @Test
    void builder_WithEmptyIsbn_ShouldFailValidation() {
               
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            BookBuilder.builder()
                    .title("Test Title")
                    .author("Test Author")
                    .isbn("   ")
                    .validate()
                    .build();
        });
        assertEquals("ISBN is required", exception.getMessage());
    }

    @Test
    void builder_MethodChaining_ShouldReturnSameInstance() {
           
        BookBuilder builder = BookBuilder.builder();

          
        BookBuilder result = builder
                .title("Test Title")
                .author("Test Author")
                .isbn("1234567890");

         
        assertSame(builder, result);
    }
}

