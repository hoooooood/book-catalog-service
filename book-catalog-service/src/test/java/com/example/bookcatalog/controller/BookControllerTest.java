package com.example.bookcatalog.controller;

import com.example.bookcatalog.decorator.LoggingBookServiceDecorator;
import com.example.bookcatalog.entity.Book;
import com.example.bookcatalog.repository.BookRepository;
import com.example.bookcatalog.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private LoggingBookServiceDecorator loggingBookService;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book testBook;

    @BeforeEach
    void setUp() {
        testBook = new Book("Test Title", "Test Author", "1234567890", 2023, new BigDecimal("29.99"));
        testBook.setId(1L);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() throws Exception {
             
        List<Book> books = List.of(testBook);
        when(loggingBookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Title"))
                .andExpect(jsonPath("$[0].author").value("Test Author"));

        verify(loggingBookService).getAllBooks();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
             
        when(loggingBookService.getBookById(1L)).thenReturn(Optional.of(testBook));

             
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Title"));

        verify(loggingBookService).getBookById(1L);
    }

    @Test
    void getBookById_WhenBookDoesNotExist_ShouldReturnNotFound() throws Exception {
             
        when(loggingBookService.getBookById(1L)).thenReturn(Optional.empty());

             
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isNotFound());

        verify(loggingBookService).getBookById(1L);
    }

    @Test
    void getBookByIsbn_WhenBookExists_ShouldReturnBook() throws Exception {
             
        String isbn = "1234567890";
        when(loggingBookService.getBookByIsbn(isbn)).thenReturn(Optional.of(testBook));

             
        mockMvc.perform(get("/api/books/isbn/" + isbn))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isbn").value(isbn));

        verify(loggingBookService).getBookByIsbn(isbn);
    }

    @Test
    void getBooksByAuthor_ShouldReturnBooksByAuthor() throws Exception {
             
        String author = "Test Author";
        List<Book> books = List.of(testBook);
        when(loggingBookService.getBooksByAuthor(author)).thenReturn(books);

             
        mockMvc.perform(get("/api/books/author/" + author))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].author").value(author));

        verify(loggingBookService).getBooksByAuthor(author);
    }

    @Test
    void searchBooksByTitle_ShouldReturnMatchingBooks() throws Exception {
             
        String title = "Test";
        List<Book> books = List.of(testBook);
        when(loggingBookService.searchBooksByTitle(title)).thenReturn(books);

             
        mockMvc.perform(get("/api/books/search").param("title", title))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Test Title"));

        verify(loggingBookService).searchBooksByTitle(title);
    }

    @Test
    void createBook_WithValidBook_ShouldReturnCreatedBook() throws Exception {
             
        Book newBook = new Book("New Title", "New Author", "0987654321", 2024, new BigDecimal("39.99"));
        when(loggingBookService.saveBookWithBuilder(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyDouble()
                )
        ).thenReturn(testBook);

             
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("title", newBook.getTitle())
                .param("author", newBook.getAuthor())
                .param("isbn", newBook.getIsbn())
                .param("publicationYear", newBook.getPublicationYear().toString())
                .param("price", newBook.getPrice().toString()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(loggingBookService).saveBookWithBuilder(
                anyString(),
                anyString(),
                anyString(),
                anyInt(),
                anyDouble()
        );
    }

    @Test
    void createBook_WithInvalidBook_ShouldReturnBadRequest() throws Exception {
             
        Book invalidBook = new Book("", "", "", null, null);
        when(loggingBookService
                .saveBookWithBuilder(
                        anyString(),
                        anyString(),
                        anyString(),
                        anyInt(),
                        anyDouble()
                )
        ).thenThrow(new IllegalArgumentException("Invalid book"));

             
        mockMvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                        .param("title", invalidBook.getTitle())
                        .param("author", invalidBook.getAuthor())
                        .param("isbn", invalidBook.getIsbn())
                        .param("publicationYear", "test")
                        .param("price", "test"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBook_WithValidBook_ShouldReturnUpdatedBook() throws Exception {
             
        Book updatedBook = new Book("Updated Title", "Updated Author", "1234567890", 2024, new BigDecimal("49.99"));
        when(loggingBookService.updateBook(eq(1L), any(Book.class))).thenReturn(testBook);

             
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));

        verify(loggingBookService).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void updateBook_WhenBookNotFound_ShouldReturnNotFound() throws Exception {
             
        Book updatedBook = new Book("Updated Title", "Updated Author", "1234567890", 2024, new BigDecimal("49.99"));
        when(loggingBookService.updateBook(eq(1L), any(Book.class))).thenThrow(new RuntimeException("Book not found"));

             
        mockMvc.perform(put("/api/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isNotFound());

        verify(loggingBookService).updateBook(eq(1L), any(Book.class));
    }

    @Test
    void deleteBook_WhenBookExists_ShouldReturnNoContent() throws Exception {
             
        doNothing().when(loggingBookService).deleteBook(1L);

             
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNoContent());

        verify(loggingBookService).deleteBook(1L);
    }

    @Test
    void deleteBook_WhenBookNotFound_ShouldReturnNotFound() throws Exception {
             
        doThrow(new RuntimeException("Book not found")).when(loggingBookService).deleteBook(1L);

             
        mockMvc.perform(delete("/api/books/1"))
                .andExpect(status().isNotFound());

        verify(loggingBookService).deleteBook(1L);
    }
}

