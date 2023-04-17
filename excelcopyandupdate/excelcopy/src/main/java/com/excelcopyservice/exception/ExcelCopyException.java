package com.excelcopyservice.exception;

public class ExcelCopyException extends RuntimeException {
    public ExcelCopyException(String message) {
        super(message);
    }

    public ExcelCopyException(String message, Throwable cause) {
        super(message, cause);
    }
}
