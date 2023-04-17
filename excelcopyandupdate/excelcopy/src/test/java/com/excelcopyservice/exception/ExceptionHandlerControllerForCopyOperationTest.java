package com.excelcopyservice.exception;

import com.excelcopyservice.controller.ExcelCopyController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ExceptionHandlerControllerForCopyOperationTest {

    @InjectMocks
    private ExceptionHandlerControllerForCopyOperation exceptionHandlerControllerForCopyOperation;

    @BeforeEach
    public void setup() {
        exceptionHandlerControllerForCopyOperation = new ExceptionHandlerControllerForCopyOperation();
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void handleExcelCopyExceptionTest(){
        String message = "Error copying data";

        ExcelCopyException exception = new ExcelCopyException(message, new RuntimeException());
        ResponseEntity<Object> responseEntity = exceptionHandlerControllerForCopyOperation.handleExcelCopyException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(message, responseEntity.getBody());
    }

}