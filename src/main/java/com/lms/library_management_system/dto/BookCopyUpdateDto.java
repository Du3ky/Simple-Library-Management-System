package com.lms.library_management_system.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopyUpdateDto {

    @NotNull(message = "Availability status is required")
    private Boolean available;
}
