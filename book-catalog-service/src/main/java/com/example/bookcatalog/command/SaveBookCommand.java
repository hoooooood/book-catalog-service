package com.example.bookcatalog.command;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;

public class SaveBookCommand implements Command {
    
    private final BookRepository bookRepository;
    private final Book book;
    private Long savedBookId;
    
    public SaveBookCommand(BookRepository bookRepository, Book book) {
        this.bookRepository = bookRepository;
        this.book = book;
    }
    
    @Override
    public void execute() {
        System.out.println("Executing SaveBookCommand: Saving book with title '" + book.getTitle() + "'");
        Book savedBook = bookRepository.save(book);
        this.savedBookId = savedBook.getId();
        System.out.println("SaveBookCommand executed successfully: Book saved with ID " + savedBookId);
    }
    
    @Override
    public void undo() {
        if (savedBookId != null) {
            System.out.println("Undoing SaveBookCommand: Deleting book with ID " + savedBookId);
            bookRepository.deleteById(savedBookId);
            System.out.println("SaveBookCommand undone successfully: Book with ID " + savedBookId + " deleted");
            savedBookId = null;
        }
    }
    
    @Override
    public String getDescription() {
        return "Save book: " + (book != null ? book.getTitle() : "Unknown");
    }
    
    public Long getSavedBookId() {
        return savedBookId;
    }
}

