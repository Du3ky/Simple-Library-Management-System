package com.lms.library_management_system.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDetailsDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
    private List<BookCopyDto> copies;
}
