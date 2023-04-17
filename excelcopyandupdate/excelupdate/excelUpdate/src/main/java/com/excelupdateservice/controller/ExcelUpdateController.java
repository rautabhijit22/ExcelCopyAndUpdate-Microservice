package com.excelupdateservice.controller;


import com.excelupdateservice.exception.ExcelUpdateException;
import com.excelupdateservice.service.ExcelUpdateService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RequestMapping("/excel")
@RestController
public class ExcelUpdateController {

    Logger logger = LoggerFactory.getLogger(ExcelUpdateController.class);
    @Value("${sheetName}")
    private String sheetName;
    private ExcelUpdateService excelUpdateService;

    @Autowired
    public ExcelUpdateController(ExcelUpdateService excelUpdateService) {
        this.excelUpdateService = excelUpdateService;
    }

    public ExcelUpdateController() {
    }

    /**
     * @param excelPath checks if any updates are made to this Excel sheet
     * @return Response Entity message based on changes made or not
     * this controller method handles update request received from excelcopy microservice
     */
    @PostMapping("/update")
    public ResponseEntity<String> handleExcelUpdateEvent(@RequestParam("filePath") String excelPath) throws ExcelUpdateException, IOException {

        //reading Excel file and store data
        XSSFWorkbook workbook;
        List<List<Object>> dataFromSheet;
        try (FileInputStream fileInputStream = new FileInputStream(excelPath)) {
            workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet sheet = workbook.getSheet(sheetName);
            dataFromSheet = excelUpdateService.readDataFromSheet(sheet);
        }

        //check if changes made
        boolean hasChangesMade = true;

        //update excel if changes made
        if (hasChangesMade) {
            FileOutputStream fileOutputStream = new FileOutputStream(excelPath);
            excelUpdateService.updateSheetWithCopiedData(dataFromSheet, sheetName, fileOutputStream);
            fileOutputStream.close();
            return ResponseEntity.ok("Excel file updated successfully");
        } else {
            logger.info("no changes found");
            return ResponseEntity.ok("Excel file has no changes");
        }

    }
}
