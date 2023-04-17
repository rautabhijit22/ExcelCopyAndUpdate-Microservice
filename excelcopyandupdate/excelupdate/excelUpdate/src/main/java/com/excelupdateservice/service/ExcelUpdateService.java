package com.excelupdateservice.service;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.io.FileOutputStream;
import java.util.List;

public interface ExcelUpdateService {
    List<List<Object>> readDataFromSheet(XSSFSheet sheet);

    void updateSheetWithCopiedData(List<List<Object>> dataFromSheet, String sheet1, FileOutputStream fileOutputStream);
}
