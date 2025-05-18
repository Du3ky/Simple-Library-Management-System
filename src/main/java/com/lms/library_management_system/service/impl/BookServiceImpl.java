package com.lms.library_management_system.service.impl;

import com.lms.library_management_system.dto.*;
import com.lms.library_management_system.entity.Book;
import com.lms.library_management_system.entity.BookCopy;
import com.lms.library_management_system.exception.BookCopyMismatchException;
import com.lms.library_management_system.exception.BookNotFoundException;
import com.lms.library_management_system.exception.CopyNotFoundException;
import com.lms.library_management_system.exception.DuplicateBookException;
import com.lms.library_management_system.repository.BookCopyRepository;
import com.lms.library_management_system.repository.BookRepository;
import com.lms.library_management_system.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookCopyRepository bookCopyRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto createBook(BookCreateDto dto) {
        if (bookRepository.existsByIsbn(dto.getIsbn()) || bookRepository.existsByTitle(dto.getTitle())) {
            throw new DuplicateBookException("Book with same title or ISBN already exists.");
        }

        Book book = Book.builder()
                .title(dto.getTitle())
                .author(dto.getAuthor())
                .isbn(dto.getIsbn())
                .publishedYear(dto.getPublishedYear())
                .build();

        return mapToBookDto(bookRepository.save(book));
    }

    @Override
    public BookDetailsDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return mapToBookDetailsDto(book);
    }

    @Override
    public BookDto updateBook(Long id, BookUpdateDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            book.setTitle(dto.getTitle());
        }

        if (dto.getPublishedYear() != null) {
            book.setPublishedYear(dto.getPublishedYear());
        }

        return mapToBookDto(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        bookRepository.delete(book);
    }

    @Override
    public List<BookCopyDto> getCopiesByBookId(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }

        return bookCopyRepository.findByBookId(id).stream()
                .map(copy -> new BookCopyDto(copy.getId(), copy.getAvailable()))
                .collect(Collectors.toList());
    }

    @Override
    public BookCopyDto addCopyToBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(bookId));

        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setAvailable(true);

        BookCopy saved = bookCopyRepository.save(copy);

        return new BookCopyDto(saved.getId(), saved.getAvailable());
    }

    @Override
    public BookCopyDto updateCopyAvailability(Long bookId, Long copyId, BookCopyUpdateDto dto) {
        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new CopyNotFoundException(copyId));

        if (!copy.getBook().getId().equals(bookId)) {
            throw new BookCopyMismatchException();
        }

        copy.setAvailable(dto.getAvailable());
        BookCopy updated = bookCopyRepository.save(copy);
        return new BookCopyDto(updated.getId(), updated.getAvailable());
    }

    private BookDto mapToBookDto(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publishedYear(book.getPublishedYear())
                .build();
    }

    private BookDetailsDto mapToBookDetailsDto(Book book) {
        List<BookCopyDto> copies = book.getCopies().stream()
                .map(copy -> new BookCopyDto(copy.getId(), copy.getAvailable()))
                .collect(Collectors.toList());

        return BookDetailsDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publishedYear(book.getPublishedYear())
                .copies(copies)
                .build();
    }

}
