package com.excelupdateservice.exception;

import com.excelupdateservice.service.ExcelUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerControllerForUpdateOperation {

    @ExceptionHandler(value = ExcelUpdateException.class)
    public ResponseEntity<Object> handleExcelUpdateException(ExcelUpdateException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
