package com.lms.library_management_system.repository;

import com.lms.library_management_system.entity.BookCopy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBookId(Long bookId);
    long countByBookIdAndAvailableTrue(Long bookId);
}
