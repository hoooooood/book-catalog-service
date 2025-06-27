package com.example.bookcatalog.strategy;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchStrategyTest {

    @Mock
    private BookRepository bookRepository;

    private TitleSearchStrategy titleSearchStrategy;
    private AuthorSearchStrategy authorSearchStrategy;
    private IsbnSearchStrategy isbnSearchStrategy;
    private Book testBook;

    @BeforeEach
    void setUp() {
        titleSearchStrategy = new TitleSearchStrategy();
        authorSearchStrategy = new AuthorSearchStrategy();
        isbnSearchStrategy = new IsbnSearchStrategy();
        testBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
    }

    @Test
    void titleSearchStrategy_ShouldSearchByTitle() {
        
        String searchTerm = "Test";
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByTitleContainingIgnoreCase(searchTerm)).thenReturn(expectedBooks);

        
        List<Book> result = titleSearchStrategy.search(searchTerm, bookRepository);

        
        assertEquals(expectedBooks, result);
        verify(bookRepository).findByTitleContainingIgnoreCase(searchTerm);
    }

    @Test
    void authorSearchStrategy_ShouldSearchByAuthor() {
        
        String searchTerm = "Test Author";
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findByAuthor(searchTerm)).thenReturn(expectedBooks);

        
        List<Book> result = authorSearchStrategy.search(searchTerm, bookRepository);

        
        assertEquals(expectedBooks, result);
        verify(bookRepository).findByAuthor(searchTerm);
    }

    @Test
    void isbnSearchStrategy_WhenBookExists_ShouldReturnSingletonList() {
        
        String searchTerm = "1234567890";
        when(bookRepository.findByIsbn(searchTerm)).thenReturn(Optional.of(testBook));

        
        List<Book> result = isbnSearchStrategy.search(searchTerm, bookRepository);

        
        assertEquals(1, result.size());
        assertEquals(testBook, result.get(0));
        verify(bookRepository).findByIsbn(searchTerm);
    }

    @Test
    void isbnSearchStrategy_WhenBookDoesNotExist_ShouldReturnEmptyList() {
        
        String searchTerm = "9999999999";
        when(bookRepository.findByIsbn(searchTerm)).thenReturn(Optional.empty());

        
        List<Book> result = isbnSearchStrategy.search(searchTerm, bookRepository);

        
        assertEquals(Collections.emptyList(), result);
        verify(bookRepository).findByIsbn(searchTerm);
    }
}

