package com.lms.library_management_system.repository;

import com.lms.library_management_system.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    boolean existsByIsbn(String isbn);
    boolean existsByTitle(String title);
}
