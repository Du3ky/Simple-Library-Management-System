package com.lms.library_management_system.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCopyDto {
    private Long id;
    private Boolean available;
}
