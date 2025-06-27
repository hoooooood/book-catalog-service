package com.example.bookcatalog.command;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;

public class DeleteBookCommand implements Command {
    
    private final BookRepository bookRepository;
    private final Long bookId;
    private Book deletedBook;
    
    public DeleteBookCommand(BookRepository bookRepository, Long bookId) {
        this.bookRepository = bookRepository;
        this.bookId = bookId;
    }
    
    @Override
    public void execute() {
        System.out.println("Executing DeleteBookCommand: Deleting book with ID " + bookId);
        this.deletedBook = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        bookRepository.deleteById(bookId);
        System.out.println("DeleteBookCommand executed successfully: Book with ID " + bookId + " deleted");
    }
    
    @Override
    public void undo() {
        if (deletedBook != null) {
            System.out.println("Undoing DeleteBookCommand: Restoring book with ID " + bookId);

            Book bookToRestore = new Book();
            bookToRestore.setTitle(deletedBook.getTitle());
            bookToRestore.setAuthor(deletedBook.getAuthor());
            bookToRestore.setIsbn(deletedBook.getIsbn());
            bookToRestore.setPublicationYear(deletedBook.getPublicationYear());
            bookToRestore.setPrice(deletedBook.getPrice());
            
            Book restoredBook = bookRepository.save(bookToRestore);
            System.out.println("DeleteBookCommand undone successfully: Book restored with new ID " + restoredBook.getId());
        }
    }
    
    @Override
    public String getDescription() {
        return "Delete book with ID: " + bookId;
    }
    
    public Book getDeletedBook() {
        return deletedBook;
    }
}

