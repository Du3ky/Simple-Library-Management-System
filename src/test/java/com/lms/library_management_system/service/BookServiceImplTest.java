package com.lms.library_management_system.service;

import com.lms.library_management_system.dto.*;
import com.lms.library_management_system.entity.Book;
import com.lms.library_management_system.entity.BookCopy;
import com.lms.library_management_system.exception.BookNotFoundException;
import com.lms.library_management_system.exception.CopyNotFoundException;
import com.lms.library_management_system.exception.DuplicateBookException;
import com.lms.library_management_system.repository.BookRepository;
import com.lms.library_management_system.repository.BookCopyRepository;
import com.lms.library_management_system.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    private BookRepository bookRepository;
    private BookCopyRepository bookCopyRepository;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        bookRepository = mock(BookRepository.class);
        bookCopyRepository = mock(BookCopyRepository.class);
        bookService = new BookServiceImpl(bookRepository, bookCopyRepository);
    }

    //getAllBooks test
    @Test
    void shouldReturnListOfBookDtos() {
        Book book1 = Book.builder()
                .id(1L)
                .title("Effective Java")
                .author("Joshua Bloch")
                .isbn("9780134685991")
                .publishedYear(2018)
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .publishedYear(2008)
                .build();

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<BookDto> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Effective Java", result.get(0).getTitle());
        assertEquals("Clean Code", result.get(1).getTitle());
    }

    //createBook test
    @Test
    void shouldCreateBookSuccessfully() {
        BookCreateDto dto = BookCreateDto.builder()
                .title("The Silmarillion")
                .author("J. R. R. Tolkien")
                .isbn("9780048231390")
                .publishedYear(1977)
                .build();

        when(bookRepository.existsByIsbn(dto.getIsbn())).thenReturn(false);
        when(bookRepository.existsByTitle(dto.getTitle())).thenReturn(false);

        Book savedBook = Book.builder()
                .id(1L)
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publishedYear(dto.getPublishedYear())
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookDto result = bookService.createBook(dto);

        assertNotNull(result);
        assertEquals("The Silmarillion", result.getTitle());
        assertEquals("J. R. R. Tolkien", result.getAuthor());
        assertEquals("9780048231390", result.getIsbn());
        assertEquals(1977, result.getPublishedYear());
    }

    @Test
    void shouldThrowExceptionWhenBookAlreadyExists() {
        BookCreateDto dto = BookCreateDto.builder()
                .title("Duplicate Book")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        when(bookRepository.existsByIsbn(dto.getIsbn())).thenReturn(true);

        assertThrows(DuplicateBookException.class, () -> bookService.createBook(dto));
    }

    //getBookById test
    @Test
    void shouldReturnBookDtoWhenBookExists() {
        Book book = Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .publishedYear(2008)
                .build();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookDetailsDto result = bookService.getBookById(1L);

        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthor());
        assertEquals("9780132350884", result.getIsbn());
        assertEquals(2008, result.getPublishedYear());
    }

    @Test
    void shouldThrowBookNotFoundExceptionWhenBookDoesNotExist() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(999L));
    }

    //updateBook test
    @Test
    void shouldUpdateBookSuccessfully() {
        Long bookId = 1L;

        BookUpdateDto dto = BookUpdateDto.builder()
                .title("Updated Title")
                .publishedYear(2024)
                .build();

        Book existingBook = Book.builder()
                .id(bookId)
                .title("Original Title")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookDto result = bookService.updateBook(bookId, dto);

        assertEquals("Updated Title", result.getTitle());
        assertEquals(2024, result.getPublishedYear());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentBook() {
        Long nonExistentId = 999L;

        BookUpdateDto dto = BookUpdateDto.builder()
                .title("Whatever")
                .publishedYear(2020)
                .build();

        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(nonExistentId, dto));
    }

    //deleteBook test
    @Test
    void shouldDeleteBookSuccessfully() {
        Long bookId = 1L;

        Book book = Book.builder()
                .id(bookId)
                .title("Book to Delete")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        bookService.deleteBook(bookId);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentBook() {
        Long nonExistentId = 999L;

        when(bookRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(nonExistentId));
    }

    //getCopiesByBookId test
    @Test
    void shouldReturnCopiesOfExistingBook() {
        Long bookId = 1L;

        BookCopy copy1 = new BookCopy();
        copy1.setId(101L);
        copy1.setAvailable(true);

        BookCopy copy2 = new BookCopy();
        copy2.setId(102L);
        copy2.setAvailable(false);

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookCopyRepository.findByBookId(bookId)).thenReturn(List.of(copy1, copy2));

        List<BookCopyDto> result = bookService.getCopiesByBookId(bookId);

        assertEquals(2, result.size());
        assertEquals(101L, result.get(0).getId());
        assertTrue(result.get(0).getAvailable());
        assertEquals(102L, result.get(1).getId());
        assertFalse(result.get(1).getAvailable());
    }

    @Test
    void shouldThrowExceptionWhenBookDoesNotExistWhileGettingCopies() {
        Long nonExistentBookId = 999L;
        when(bookRepository.existsById(nonExistentBookId)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.getCopiesByBookId(nonExistentBookId));
    }

    //addCopyToBook test
    @Test
    void shouldAddCopyToExistingBook() {
        Long bookId = 1L;

        Book book = Book.builder()
                .id(bookId)
                .title("Test Book")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        BookCopy savedCopy = new BookCopy();
        savedCopy.setId(100L);
        savedCopy.setBook(book);
        savedCopy.setAvailable(true);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(savedCopy);

        BookCopyDto result = bookService.addCopyToBook(bookId);

        assertEquals(100L, result.getId());
        assertTrue(result.getAvailable());
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundForCopy() {
        Long invalidBookId = 999L;
        when(bookRepository.findById(invalidBookId)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.addCopyToBook(invalidBookId));
    }

    //updateCopyAvailability test
    @Test
    void shouldUpdateAvailabilityOfValidBookCopy() {
        Long bookId = 1L;
        Long copyId = 10L;

        Book book = Book.builder()
                .id(bookId)
                .title("Test Book")
                .author("Author")
                .isbn("1234567654321")
                .publishedYear(2025)
                .build();

        BookCopy copy = new BookCopy();
        copy.setId(copyId);
        copy.setBook(book);
        copy.setAvailable(true);

        BookCopy updated = new BookCopy();
        updated.setId(copyId);
        updated.setBook(book);
        updated.setAvailable(false);

        BookCopyUpdateDto dto = new BookCopyUpdateDto();
        dto.setAvailable(false);

        when(bookCopyRepository.findById(copyId)).thenReturn(Optional.of(copy));
        when(bookCopyRepository.save(any(BookCopy.class))).thenReturn(updated);

        BookCopyDto result = bookService.updateCopyAvailability(bookId, copyId, dto);

        assertEquals(copyId, result.getId());
        assertFalse(result.getAvailable());
    }

    @Test
    void shouldThrowWhenCopyNotFound() {
        Long bookId = 1L;
        Long copyId = 999L;

        when(bookCopyRepository.findById(copyId)).thenReturn(Optional.empty());

        BookCopyUpdateDto dto = new BookCopyUpdateDto();
        dto.setAvailable(false);

        assertThrows(CopyNotFoundException.class, () -> bookService.updateCopyAvailability(bookId, copyId, dto));
    }

    @Test
    void shouldThrowWhenCopyBelongsToAnotherBook() {
        Long requestedBookId = 1L;
        Long copyId = 10L;

        Book differentBook = Book.builder()
                .id(2L)
                .title("Other Book")
                .author("Different Author")
                .isbn("9999999999999")
                .publishedYear(2019)
                .build();

        BookCopy copy = new BookCopy();
        copy.setId(copyId);
        copy.setBook(differentBook);
        copy.setAvailable(true);

        BookCopyUpdateDto dto = new BookCopyUpdateDto();
        dto.setAvailable(false);

        when(bookCopyRepository.findById(copyId)).thenReturn(Optional.of(copy));

        assertThrows(IllegalArgumentException.class, () -> bookService.updateCopyAvailability(requestedBookId, copyId, dto));
    }

}
