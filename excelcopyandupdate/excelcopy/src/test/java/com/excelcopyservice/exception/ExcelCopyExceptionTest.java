package com.excelcopyservice.exception;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
class ExcelCopyExceptionTest {

    @Test
    void testExcelCopyException() {
        String message = "Error copying data";
        Throwable cause = new Throwable("Test cause");
        ExcelCopyException exception = new ExcelCopyException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}