package com.example.bookcatalog.service;

import com.example.bookcatalog.command.CommandInvoker;
import com.example.bookcatalog.command.DeleteBookCommand;
import com.example.bookcatalog.command.SaveBookCommand;
import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.strategy.AuthorSearchStrategy;
import com.example.bookcatalog.strategy.IsbnSearchStrategy;
import com.example.bookcatalog.strategy.SearchStrategyFactory;
import com.example.bookcatalog.strategy.TitleSearchStrategy;
import com.example.bookcatalog.validator.BookValidatorFactory;
import com.example.bookcatalog.validator.StandardBookValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private SearchStrategyFactory searchStrategyFactory;

    @Mock
    private BookValidatorFactory bookValidatorFactory;

    @Mock
    private StandardBookValidator standardBookValidator;

    @Mock
    private TitleSearchStrategy titleSearchStrategy;

    @Mock
    private AuthorSearchStrategy authorSearchStrategy;

    @Mock
    private IsbnSearchStrategy isbnSearchStrategy;

    @Mock
    private CommandInvoker commandInvoker;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        testBook.setId(1L);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        
        List<Book> expectedBooks = List.of(testBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        
        List<Book> actualBooks = bookService.getAllBooks();

        
        assertEquals(expectedBooks, actualBooks);
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        
        Optional<Book> result = bookService.getBookById(1L);

        
        assertTrue(result.isPresent());
        assertEquals(testBook, result.get());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnEmpty() {
        
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        
        Optional<Book> result = bookService.getBookById(1L);

        
        assertFalse(result.isPresent());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookByIsbn_ShouldReturnBook() {
        
        String isbn = "1234567890";
        when(searchStrategyFactory.getSearchStrategy(anyString())).thenReturn(isbnSearchStrategy);
        when(isbnSearchStrategy.search(isbn, bookRepository)).thenReturn(List.of(testBook));

        Optional<Book> result = bookService.getBookByIsbn(isbn);

        assertTrue(result.isPresent());
        assertEquals(testBook, result.get());
        verify(isbnSearchStrategy).search(isbn, bookRepository);
    }

    @Test
    void getBooksByAuthor_ShouldReturnBooksByAuthor() {
        
        String author = "Test Author";
        List<Book> expectedBooks = List.of(testBook);
        when(searchStrategyFactory.getSearchStrategy(anyString())).thenReturn(authorSearchStrategy);
        when(authorSearchStrategy.search(author, bookRepository)).thenReturn(expectedBooks);
        
        List<Book> result = bookService.getBooksByAuthor(author);

        
        assertEquals(expectedBooks, result);
        verify(authorSearchStrategy).search(author, bookRepository);
    }

    @Test
    void searchBooksByTitle_ShouldReturnMatchingBooks() {
        
        String title = "Test";
        List<Book> expectedBooks = List.of(testBook);
        when(searchStrategyFactory.getSearchStrategy(anyString())).thenReturn(titleSearchStrategy);
        when(titleSearchStrategy.search(title, bookRepository)).thenReturn(expectedBooks);

        List<Book> result = bookService.searchBooksByTitle(title);

        assertEquals(expectedBooks, result);
        verify(titleSearchStrategy).search(title, bookRepository);
    }

    @Test
    void searchBooks_ShouldUseCorrectStrategy() {
        
        String searchType = "title";
        String searchTerm = "Test";
        List<Book> expectedBooks = List.of(testBook);
        
        when(searchStrategyFactory.getSearchStrategy(searchType)).thenReturn(titleSearchStrategy);
        when(titleSearchStrategy.search(searchTerm, bookRepository)).thenReturn(expectedBooks);

        
        List<Book> result = bookService.searchBooks(searchType, searchTerm);

        
        assertEquals(expectedBooks, result);
        verify(searchStrategyFactory).getSearchStrategy(searchType);
        verify(titleSearchStrategy).search(searchTerm, bookRepository);
    }

    @Test
    void saveBook_ShouldValidateAndSaveBook() {
        
        when(bookValidatorFactory.getValidator("standard")).thenReturn(standardBookValidator);
        doNothing().when(standardBookValidator).validate(testBook);
        doAnswer(invocation -> {
            SaveBookCommand command = invocation.getArgument(0);
            setCommandSavedBookId(command, testBook.getId());
            return command;
        }).when(commandInvoker).executeCommand(any(SaveBookCommand.class));
        when(bookRepository.findById(testBook.getId())).thenReturn(Optional.of(testBook));
        
        Book result = bookService.saveBook(testBook);

        assertEquals(testBook, result);
        verify(bookValidatorFactory).getValidator("standard");
        verify(standardBookValidator).validate(testBook);
        verify(commandInvoker).executeCommand(any(SaveBookCommand.class));
        verify(bookRepository).findById(testBook.getId());
    }

    private void setCommandSavedBookId(SaveBookCommand command, Long id) {
        try {
            Field field = SaveBookCommand.class.getDeclaredField("savedBookId");
            field.setAccessible(true);
            field.set(command, id);
        } catch (Exception e) {
            System.err.println("Error setting savedBookId in SaveBookCommand: " + e.getMessage());
        }
    }

    @Test
    void updateBook_WhenBookExists_ShouldUpdateAndReturnBook() {
        
        Long bookId = 1L;
        Book updatedDetails = new Book("Updated Title", "Updated Author", "0987654321", 2024, new BigDecimal("39.99"));
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        when(bookValidatorFactory.getValidator("update")).thenReturn(standardBookValidator);
        doNothing().when(standardBookValidator).validate(updatedDetails);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        
        Book result = bookService.updateBook(bookId, updatedDetails);

        
        assertNotNull(result);
        verify(bookRepository).findById(bookId);
        verify(bookValidatorFactory).getValidator("update");
        verify(standardBookValidator).validate(updatedDetails);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookDoesNotExist_ShouldThrowException() {
        
        Long bookId = 1L;
        Book updatedDetails = new Book("Updated Title", "Updated Author", "0987654321", 2024, new BigDecimal("39.99"));
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

         
        assertThrows(RuntimeException.class, () -> bookService.updateBook(bookId, updatedDetails));
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldDeleteBook() {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).deleteById(bookId);

        doAnswer(invocation -> {
            DeleteBookCommand command = invocation.getArgument(0);
            setCommandDeletedBookId(command, bookId);
            command.execute();
            return command;
        }).when(commandInvoker).executeCommand(any(DeleteBookCommand.class));

        bookService.deleteBook(bookId);

        verify(bookRepository).findById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void deleteBook_WhenBookDoesNotExist_ShouldThrowException() {
        
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        doAnswer(invocation -> {
            DeleteBookCommand command = invocation.getArgument(0);
            setCommandDeletedBookId(command, bookId);
            command.execute();
            return command;
        }).when(commandInvoker).executeCommand(any(DeleteBookCommand.class));

         
        assertThrows(RuntimeException.class, () -> bookService.deleteBook(bookId));
        verify(bookRepository).findById(bookId);
        verify(bookRepository, never()).delete(any(Book.class));
    }

    private void setCommandDeletedBookId(DeleteBookCommand command, Long id) {
        try {
            Field field = DeleteBookCommand.class.getDeclaredField("bookId");
            field.setAccessible(true);
            field.set(command, id);
        } catch (Exception e) {
            System.err.println("Error setting bookId in DeletedBookCommand: " + e.getMessage());
        }
    }
}

