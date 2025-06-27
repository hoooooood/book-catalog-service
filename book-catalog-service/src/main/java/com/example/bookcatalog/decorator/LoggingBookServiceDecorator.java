package com.example.bookcatalog.decorator;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LoggingBookServiceDecorator implements BookServiceDecorator {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingBookServiceDecorator.class);
    
    private final BookService bookService;
    
    public LoggingBookServiceDecorator(BookService bookService) {
        this.bookService = bookService;
    }
    
    @Override
    public List<Book> getAllBooks() {
        long startTime = System.currentTimeMillis();
        logMethodEntry("getAllBooks");
        
        try {
            List<Book> result = bookService.getAllBooks();
            long endTime = System.currentTimeMillis();
            logMethodExit("getAllBooks", endTime - startTime, result.size() + " books retrieved");
            return result;
        } catch (Exception e) {
            logMethodError("getAllBooks", e);
            throw e;
        }
    }
    
    @Override
    public Optional<Book> getBookById(Long id) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("getBookById", "id=" + id);
        
        try {
            Optional<Book> result = bookService.getBookById(id);
            long endTime = System.currentTimeMillis();
            logMethodExit("getBookById", endTime - startTime, 
                result.isPresent() ? "Book found" : "Book not found");
            return result;
        } catch (Exception e) {
            logMethodError("getBookById", e);
            throw e;
        }
    }
    
    @Override
    public Optional<Book> getBookByIsbn(String isbn) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("getBookByIsbn", "isbn=" + isbn);
        
        try {
            Optional<Book> result = bookService.getBookByIsbn(isbn);
            long endTime = System.currentTimeMillis();
            logMethodExit("getBookByIsbn", endTime - startTime, 
                result.isPresent() ? "Book found" : "Book not found");
            return result;
        } catch (Exception e) {
            logMethodError("getBookByIsbn", e);
            throw e;
        }
    }
    
    @Override
    public List<Book> getBooksByAuthor(String author) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("getBooksByAuthor", "author=" + author);
        
        try {
            List<Book> result = bookService.getBooksByAuthor(author);
            long endTime = System.currentTimeMillis();
            logMethodExit("getBooksByAuthor", endTime - startTime, 
                result.size() + " books found for author");
            return result;
        } catch (Exception e) {
            logMethodError("getBooksByAuthor", e);
            throw e;
        }
    }
    
    @Override
    public List<Book> searchBooksByTitle(String title) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("searchBooksByTitle", "title=" + title);
        
        try {
            List<Book> result = bookService.searchBooksByTitle(title);
            long endTime = System.currentTimeMillis();
            logMethodExit("searchBooksByTitle", endTime - startTime, 
                result.size() + " books found matching title");
            return result;
        } catch (Exception e) {
            logMethodError("searchBooksByTitle", e);
            throw e;
        }
    }
    
    @Override
    public List<Book> searchBooks(String searchType, String searchTerm) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("searchBooks", "searchType=" + searchType + ", searchTerm=" + searchTerm);
        
        try {
            List<Book> result = bookService.searchBooks(searchType, searchTerm);
            long endTime = System.currentTimeMillis();
            logMethodExit("searchBooks", endTime - startTime, 
                result.size() + " books found");
            return result;
        } catch (Exception e) {
            logMethodError("searchBooks", e);
            throw e;
        }
    }
    
    @Override
    public Book saveBookWithBuilder(String title, String author, String isbn, Integer publicationYear, Double price) {
        long startTime = System.currentTimeMillis();

        logMethodEntry("saveBook", "book=" + title);
        try {
            Book result = bookService.saveBookWithBuilder(title, author, isbn, publicationYear, price);;
            long endTime = System.currentTimeMillis();
            logMethodExit("saveBook", endTime - startTime, 
                "Book saved with ID " + result.getId());
            return result;
        } catch (Exception e) {
            logMethodError("saveBook", e);
            throw e;
        }
    }
    
    @Override
    public Book updateBook(Long id, Book bookDetails) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("updateBook", "id=" + id + ", book=" + 
            (bookDetails != null ? bookDetails.getTitle() : "null"));
        
        try {
            Book result = bookService.updateBook(id, bookDetails);
            long endTime = System.currentTimeMillis();
            logMethodExit("updateBook", endTime - startTime, 
                "Book updated successfully");
            return result;
        } catch (Exception e) {
            logMethodError("updateBook", e);
            throw e;
        }
    }
    
    @Override
    public void deleteBook(Long id) {
        long startTime = System.currentTimeMillis();
        logMethodEntry("deleteBook", "id=" + id);
        
        try {
            bookService.deleteBook(id);
            long endTime = System.currentTimeMillis();
            logMethodExit("deleteBook", endTime - startTime, 
                "Book deleted successfully");
        } catch (Exception e) {
            logMethodError("deleteBook", e);
            throw e;
        }
    }
    
    private void logMethodEntry(String methodName) {
        logMethodEntry(methodName, null);
    }
    
    private void logMethodEntry(String methodName, String parameters) {
        String message = "Entering method: " + methodName;
        if (parameters != null) {
            message += " with parameters: " + parameters;
        }
        logger.info(message);
    }
    
    private void logMethodExit(String methodName, long executionTime, String result) {
        logger.info("Exiting method: {} - Execution time: {}ms - Result: {}",
            methodName, executionTime, result);

    }
    
    private void logMethodError(String methodName, Exception e) {
        logger.error("Error in method: {} - Exception: {}", methodName, e.getMessage(), e);
    }

}

