package com.example.bookcatalog.command;

import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommandTest {

    @Mock
    private BookRepository bookRepository;

    private Book testBook;
    private CommandInvoker commandInvoker;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        testBook.setId(1L);
        commandInvoker = new CommandInvoker();
    }

    @Test
    void saveBookCommand_Execute_ShouldSaveBook() {
             
        Book savedBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        savedBook.setId(1L);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        SaveBookCommand command = new SaveBookCommand(bookRepository, testBook);

             
        command.execute();

             
        verify(bookRepository).save(testBook);
        assertEquals(1L, command.getSavedBookId());
    }

    @Test
    void saveBookCommand_Undo_ShouldDeleteSavedBook() {
             
        Book savedBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        savedBook.setId(1L);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        SaveBookCommand command = new SaveBookCommand(bookRepository, testBook);
        command.execute();

             
        command.undo();

             
        verify(bookRepository).deleteById(1L);
        assertNull(command.getSavedBookId());
    }

    @Test
    void deleteBookCommand_Execute_ShouldDeleteBook() {
             
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        DeleteBookCommand command = new DeleteBookCommand(bookRepository, 1L);

             
        command.execute();

             
        verify(bookRepository).findById(1L);
        verify(bookRepository).deleteById(1L);
        assertNotNull(command.getDeletedBook());
    }

    @Test
    void deleteBookCommand_ExecuteWithNonExistentBook_ShouldThrowException() {
             
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        DeleteBookCommand command = new DeleteBookCommand(bookRepository, 1L);

        assertThrows(RuntimeException.class, command::execute);
    }

    @Test
    void deleteBookCommand_Undo_ShouldRestoreBook() {
             
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        DeleteBookCommand command = new DeleteBookCommand(bookRepository, 1L);
        command.execute();

             
        command.undo();

             
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void commandInvoker_ExecuteCommand_ShouldAddToHistory() {
             
        SaveBookCommand command = new SaveBookCommand(bookRepository, testBook);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

             
        commandInvoker.executeCommand(command);

             
        assertEquals(1, commandInvoker.getHistorySize());
        assertTrue(commandInvoker.hasCommands());
    }

    @Test
    void commandInvoker_UndoLastCommand_ShouldUndoAndRemoveFromHistory() {
             
        SaveBookCommand command = new SaveBookCommand(bookRepository, testBook);
        Book savedBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        savedBook.setId(1L);
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        commandInvoker.executeCommand(command);

             
        commandInvoker.undoLastCommand();

             
        assertEquals(0, commandInvoker.getHistorySize());
        assertFalse(commandInvoker.hasCommands());
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void commandInvoker_GetCommandHistory_ShouldReturnDescriptions() {
             
        SaveBookCommand command1 = new SaveBookCommand(bookRepository, testBook);
        DeleteBookCommand command2 = new DeleteBookCommand(bookRepository, 1L);
        
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        commandInvoker.executeCommand(command1);
        commandInvoker.executeCommand(command2);

             
        var history = commandInvoker.getCommandHistory();

             
        assertEquals(2, history.size());
        assertTrue(history.get(0).contains("Save book"));
        assertTrue(history.get(1).contains("Delete book"));
    }

    @Test
    void commandInvoker_ClearHistory_ShouldRemoveAllCommands() {
             
        SaveBookCommand command = new SaveBookCommand(bookRepository, testBook);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        commandInvoker.executeCommand(command);

             
        commandInvoker.clearHistory();

             
        assertEquals(0, commandInvoker.getHistorySize());
        assertFalse(commandInvoker.hasCommands());
    }
}

