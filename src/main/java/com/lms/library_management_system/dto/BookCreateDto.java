package com.lms.library_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDto {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "ISBN is required")
    @Pattern(
            regexp = "^(97(8|9))?\\d{9}(\\d|X)$",
            message = "Invalid ISBN format"
    )
    private String isbn;

    @NotNull(message = "Published year is required")
    @Min(value = 1000, message = "Published year must be a valid year")
    @Max(value = 2100, message = "Published year must be realistic")
    private Integer publishedYear;

}
