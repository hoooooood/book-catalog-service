package com.example.bookcatalog.service;

import com.example.bookcatalog.builder.BookBuilder;
import com.example.bookcatalog.command.CommandInvoker;
import com.example.bookcatalog.command.DeleteBookCommand;
import com.example.bookcatalog.command.SaveBookCommand;
import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.strategy.SearchStrategy;
import com.example.bookcatalog.strategy.SearchStrategyFactory;
import com.example.bookcatalog.validator.BookValidator;
import com.example.bookcatalog.validator.BookValidatorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final SearchStrategyFactory searchStrategyFactory;
    private final BookValidatorFactory bookValidatorFactory;
    private final CommandInvoker commandInvoker;

    @Autowired
    public BookService(BookRepository bookRepository, 
                      SearchStrategyFactory searchStrategyFactory,
                      BookValidatorFactory bookValidatorFactory,
                      CommandInvoker commandInvoker) {
        this.bookRepository = bookRepository;
        this.searchStrategyFactory = searchStrategyFactory;
        this.bookValidatorFactory = bookValidatorFactory;
        this.commandInvoker = commandInvoker;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> getBookByIsbn(String isbn) {
        List<Book> bookList = searchBooks("isbn", isbn);
        return bookList.stream().findFirst();
    }

    public List<Book> getBooksByAuthor(String author) {
        return searchBooks("author", author);
    }

    public List<Book> searchBooksByTitle(String title) {
        return searchBooks("title", title);
    }

    public List<Book> searchBooks(String searchType, String searchTerm) {
        SearchStrategy strategy = searchStrategyFactory.getSearchStrategy(searchType);
        return strategy.search(searchTerm, bookRepository);
    }

    public Book saveBook(Book book) {
        BookValidator validator = bookValidatorFactory.getValidator("standard");
        validator.validate(book);

        SaveBookCommand saveCommand = new SaveBookCommand(bookRepository, book);
        commandInvoker.executeCommand(saveCommand);

        return bookRepository.findById(saveCommand.getSavedBookId()).orElse(book);
    }

    public Book saveBookWithBuilder(String title, String author, String isbn, Integer publicationYear, Double price) {
        Book book = BookBuilder.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .publicationYear(publicationYear)
                .price(price)
                .validate()
                .build();
        return saveBook(book);
    }

    public Book updateBook(Long id, Book bookDetails) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));

        BookValidator validator = bookValidatorFactory.getValidator("update");
        validator.validate(bookDetails);

        book.setTitle(bookDetails.getTitle());
        book.setAuthor(bookDetails.getAuthor());
        book.setIsbn(bookDetails.getIsbn());
        book.setPublicationYear(bookDetails.getPublicationYear());
        book.setPrice(bookDetails.getPrice());

        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        DeleteBookCommand deleteCommand = new DeleteBookCommand(bookRepository, id);
        commandInvoker.executeCommand(deleteCommand);
    }

    public void undoLastOperation() {
        commandInvoker.undoLastCommand();
    }

    public List<String> getOperationHistory() {
        return commandInvoker.getCommandHistory();
    }

    public void clearOperationHistory() {
        commandInvoker.clearHistory();
    }
}

