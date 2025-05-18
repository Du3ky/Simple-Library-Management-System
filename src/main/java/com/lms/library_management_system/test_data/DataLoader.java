package com.lms.library_management_system.test_data;

import com.lms.library_management_system.entity.Book;
import com.lms.library_management_system.entity.BookCopy;
import com.lms.library_management_system.repository.BookCopyRepository;
import com.lms.library_management_system.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;

    @Override
    public void run(String... args) {
        if (bookRepository.count() == 0) {
            Book book1 = bookRepository.save(Book.builder()
                    .title("Effective Java")
                    .author("Joshua Bloch")
                    .isbn("9780134685991")
                    .publishedYear(2018)
                    .build());

            Book book2 = bookRepository.save(Book.builder()
                    .title("Clean Code")
                    .author("Robert C. Martin")
                    .isbn("9780132350884")
                    .publishedYear(2008)
                    .build());

            bookCopyRepository.save(BookCopy.builder().book(book1).available(true).build());
            bookCopyRepository.save(BookCopy.builder().book(book1).available(false).build());
            bookCopyRepository.save(BookCopy.builder().book(book2).available(true).build());
        }
    }
}