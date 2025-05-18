package com.lms.library_management_system.service;

import com.lms.library_management_system.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {
    Page<BookDto> getAllBooks(Pageable pageable);
    BookDto createBook(BookCreateDto dto);
    BookDetailsDto getBookById(Long id);
    BookDto updateBook(Long id, BookUpdateDto dto);
    void deleteBook(Long id);

    List<BookCopyDto> getCopiesByBookId(Long id);
    BookCopyDto addCopyToBook(Long bookId);
    BookCopyDto updateCopyAvailability(Long bookId, Long copyId, BookCopyUpdateDto dto);
}
