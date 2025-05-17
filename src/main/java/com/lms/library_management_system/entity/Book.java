package com.lms.library_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, unique = true)
    private String title;

    @NotBlank(message = "Author is required")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(
            regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
            message = "Invalid ISBN format"
    )
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotNull(message = "Published year is required")
    @Min(value = 1000, message = "Published year must be a valid year")
    @Max(value = 2100, message = "Published year must be realistic")
    private Integer publishedYear;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BookCopy> copies = new ArrayList<>();

}
