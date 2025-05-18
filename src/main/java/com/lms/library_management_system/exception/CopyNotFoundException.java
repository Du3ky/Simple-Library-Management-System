package com.lms.library_management_system.exception;

public class CopyNotFoundException extends RuntimeException {
    public CopyNotFoundException(Long id) {
        super("Book copy with ID " + id + " not found");
    }
}
