package com.lms.library_management_system.exception;

public class BookCopyMismatchException extends RuntimeException {
    public BookCopyMismatchException() {
        super("This copy does not belong to the specified book.");
    }
}
