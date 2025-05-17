package com.lms.library_management_system.controller;

import com.lms.library_management_system.dto.*;
import com.lms.library_management_system.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //Endpoint 1
    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    //Endpoint 2
    @PostMapping
    public ResponseEntity<BookDto> addBook(@Valid @RequestBody BookCreateDto dto) {
        BookDto created = bookService.createBook(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    //Endpoint 3
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailsDto> getBookById(@PathVariable Long id) {
        try {
            BookDetailsDto book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint 4
    @PutMapping("/{id}")
    public ResponseEntity<BookDto> updateBook(@PathVariable Long id,
                                              @Valid @RequestBody BookUpdateDto dto) {
        try {
            BookDto updated = bookService.updateBook(id, dto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint 5
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint 6
    @GetMapping("/{id}/copies")
    public ResponseEntity<List<BookCopyDto>> getCopiesByBookId(@PathVariable Long id) {
        try {
            List<BookCopyDto> copies = bookService.getCopiesByBookId(id);
            return ResponseEntity.ok(copies);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint 7
    @PostMapping("/{id}/copies")
    public ResponseEntity<BookCopyDto> addCopyToBook(@PathVariable Long id) {
        try {
            BookCopyDto created = bookService.addCopyToBook(id);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //Endpoint 8
    @PutMapping("/{id}/copies/{copyId}")
    public ResponseEntity<BookCopyDto> updateCopyAvailability(
            @PathVariable Long id,
            @PathVariable Long copyId,
            @Valid @RequestBody BookCopyUpdateDto dto
    ) {
        try {
            BookCopyDto updated = bookService.updateCopyAvailability(id, copyId, dto);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
