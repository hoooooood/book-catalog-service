package com.example.bookcatalog.decorator;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecoratorTest {

    @Mock
    private BookService bookService;

    private LoggingBookServiceDecorator loggingDecorator;
    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        testBook.setId(1L);
        
        loggingDecorator = new LoggingBookServiceDecorator(bookService);
    }

    @Test
    void loggingDecorator_GetAllBooks_ShouldCallServiceAndReturnResult() {
        
        List<Book> expectedBooks = List.of(testBook);
        when(bookService.getAllBooks()).thenReturn(expectedBooks);

        
        List<Book> result = loggingDecorator.getAllBooks();

        
        assertEquals(expectedBooks, result);
        verify(bookService).getAllBooks();
    }

    @Test
    void loggingDecorator_GetBookById_ShouldCallServiceAndReturnResult() {
        
        when(bookService.getBookById(1L)).thenReturn(Optional.of(testBook));

        
        Optional<Book> result = loggingDecorator.getBookById(1L);

        
        assertTrue(result.isPresent());
        assertEquals(testBook, result.get());
        verify(bookService).getBookById(1L);
    }

    @Test
    void loggingDecorator_SaveBook_ShouldCallServiceAndReturnResult() {
        
        when(bookService.saveBookWithBuilder(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyDouble())
        ).thenReturn(testBook);

        
        Book result = loggingDecorator.saveBookWithBuilder(testBook.getTitle(), testBook.getAuthor(), testBook.getIsbn(),testBook.getPublicationYear(), 29.99);

        
        assertEquals(testBook, result);
        verify(bookService).saveBookWithBuilder(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyDouble()
        );
    }

    @Test
    void loggingDecorator_DeleteBook_ShouldCallService() {
        
        doNothing().when(bookService).deleteBook(1L);

        
        loggingDecorator.deleteBook(1L);

        
        verify(bookService).deleteBook(1L);
    }

    @Test
    void loggingDecorator_WithException_ShouldPropagateException() {
        
        when(bookService.getBookById(1L)).thenThrow(new RuntimeException("Test exception"));

        
        assertThrows(RuntimeException.class, () -> loggingDecorator.getBookById(1L));
        verify(bookService).getBookById(1L);
    }

}

