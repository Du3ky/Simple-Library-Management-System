package com.lms.library_management_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDto {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer publishedYear;
}
