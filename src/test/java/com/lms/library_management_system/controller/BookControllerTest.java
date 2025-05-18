package com.lms.library_management_system.controller;

import com.lms.library_management_system.dto.*;
import com.lms.library_management_system.exception.BookNotFoundException;
import com.lms.library_management_system.exception.CopyNotFoundException;
import com.lms.library_management_system.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class BookControllerTest {

    private BookService bookService;
    private BookController bookController;

    @BeforeEach
    void setUp() {
        bookService = mock(BookService.class);
        bookController = new BookController(bookService);
    }

    //getAllBooks test
    @Test
    void shouldReturnListOfBooks() {
        BookDto book1 = BookDto.builder()
                .id(1L)
                .title("Book One")
                .author("Author One")
                .isbn("1111111111111")
                .publishedYear(2000)
                .build();

        BookDto book2 = BookDto.builder()
                .id(2L)
                .title("Book Two")
                .author("Author Two")
                .isbn("2222222222222")
                .publishedYear(2010)
                .build();

        when(bookService.getAllBooks()).thenReturn(List.of(book1, book2));

        List<BookDto> result = bookController.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getTitle());
        assertEquals("Book Two", result.get(1).getTitle());
    }

    //addBook test
    @Test
    void shouldCreateBookAndReturnCreatedResponse() {
        BookCreateDto createDto = BookCreateDto.builder()
                .title("New Book")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        BookDto savedDto = BookDto.builder()
                .id(1L)
                .title("New Book")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        when(bookService.createBook(createDto)).thenReturn(savedDto);

        ResponseEntity<BookDto> response = bookController.addBook(createDto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("New Book", response.getBody().getTitle());
        assertEquals("Author", response.getBody().getAuthor());
    }

    //getBookById test
    @Test
    void shouldReturnBookDetailsDtoById() {
        Long bookId = 1L;

        BookCopyDto copy1 = new BookCopyDto(1L, true);
        BookCopyDto copy2 = new BookCopyDto(2L, false);

        BookDetailsDto dto = BookDetailsDto.builder()
                .id(bookId)
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("9780134685991")
                .publishedYear(2018)
                .copies(List.of(copy1, copy2))
                .build();

        when(bookService.getBookById(bookId)).thenReturn(dto);

        ResponseEntity<BookDetailsDto> response = bookController.getBookById(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Effective Java", response.getBody().getTitle());
        assertEquals(2, response.getBody().getCopies().size());
    }

    //updateBook test
    @Test
    void shouldUpdateBookById() {
        Long bookId = 1L;

        BookUpdateDto updateDto = BookUpdateDto.builder()
                .title("Effective Java (3rd Edition)")
                .publishedYear(2018)
                .build();

        BookDto updatedDto = BookDto.builder()
                .id(bookId)
                .title("Effective Java (3rd Edition)")
                .author("Joshua Bloch")
                .isbn("9780134685991")
                .publishedYear(2018)
                .build();

        when(bookService.updateBook(eq(bookId), eq(updateDto))).thenReturn(updatedDto);

        ResponseEntity<BookDto> response = bookController.updateBook(bookId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Effective Java (3rd Edition)", response.getBody().getTitle());
        assertEquals(2018, response.getBody().getPublishedYear());
    }

    //deleteBook test
    @Test
    void shouldDeleteBookById() {
        Long bookId = 1L;

        doNothing().when(bookService).deleteBook(bookId);

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonexistentBook() {
        Long bookId = 999L;

        doThrow(new BookNotFoundException(bookId)).when(bookService).deleteBook(bookId);

        ResponseEntity<Void> response = bookController.deleteBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //getCopiesByBookId test
    @Test
    void shouldReturnBookCopiesByBookId() {
        Long bookId = 1L;
        List<BookCopyDto> copies = List.of(
                new BookCopyDto(1L, true),
                new BookCopyDto(2L, false)
        );

        when(bookService.getCopiesByBookId(bookId)).thenReturn(copies);

        ResponseEntity<List<BookCopyDto>> response = bookController.getCopiesByBookId(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
    }

    @Test
    void shouldReturnNotFoundWhenGettingCopiesForNonexistentBook() {
        Long bookId = 999L;

        when(bookService.getCopiesByBookId(bookId))
                .thenThrow(new BookNotFoundException(bookId));

        ResponseEntity<List<BookCopyDto>> response = bookController.getCopiesByBookId(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //addCopyToBook test
    @Test
    void shouldCreateCopyForBook() {
        Long bookId = 1L;
        BookCopyDto createdCopy = new BookCopyDto(10L, true);

        when(bookService.addCopyToBook(bookId)).thenReturn(createdCopy);

        ResponseEntity<BookCopyDto> response = bookController.addCopyToBook(bookId);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10L, response.getBody().getId());
        assertTrue(response.getBody().getAvailable());
    }

    @Test
    void shouldReturnNotFoundWhenAddingCopyToNonexistentBook() {
        Long bookId = 999L;

        when(bookService.addCopyToBook(bookId))
                .thenThrow(new BookNotFoundException(bookId));

        ResponseEntity<BookCopyDto> response = bookController.addCopyToBook(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    //updateCopyAvailability test
    @Test
    void shouldUpdateCopyAvailability() {
        Long bookId = 1L;
        Long copyId = 2L;
        BookCopyUpdateDto dto = new BookCopyUpdateDto(false);

        BookCopyDto updatedCopy = new BookCopyDto(copyId, false);

        when(bookService.updateCopyAvailability(bookId, copyId, dto))
                .thenReturn(updatedCopy);

        ResponseEntity<BookCopyDto> response =
                bookController.updateCopyAvailability(bookId, copyId, dto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(copyId, response.getBody().getId());
        assertFalse(response.getBody().getAvailable());
    }

    @Test
    void shouldReturnNotFoundIfCopyDoesNotExist() {
        Long bookId = 1L;
        Long copyId = 999L;
        BookCopyUpdateDto dto = new BookCopyUpdateDto(false);

        when(bookService.updateCopyAvailability(bookId, copyId, dto))
                .thenThrow(new CopyNotFoundException(copyId));

        ResponseEntity<BookCopyDto> response =
                bookController.updateCopyAvailability(bookId, copyId, dto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequestIfCopyMismatch() {
        Long bookId = 1L;
        Long copyId = 2L;
        BookCopyUpdateDto dto = new BookCopyUpdateDto(true);

        when(bookService.updateCopyAvailability(bookId, copyId, dto))
                .thenThrow(new IllegalArgumentException("Mismatch"));

        ResponseEntity<BookCopyDto> response =
                bookController.updateCopyAvailability(bookId, copyId, dto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
