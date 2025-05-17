package com.lms.library_management_system.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUpdateDto {

    @Size(min = 1, message = "Title must not be empty")
    private String title;

    @Min(value = 1000, message = "Published year must be valid")
    @Max(value = 2100, message = "Published year must be realistic")
    private Integer publishedYear;
}
