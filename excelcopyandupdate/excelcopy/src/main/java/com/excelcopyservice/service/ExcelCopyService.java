package com.excelcopyservice.service;

public interface ExcelCopyService {
    void copyDataFromSheetToSheet(String sourcePath, String destinationPath, String Sheet1);
}
