package com.excelupdateservice.service;

import com.excelupdateservice.controller.ExcelUpdateController;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
class ExcelUpdateServiceImplTest {

    @Autowired
    private ExcelUpdateServiceImpl excelUpdateServiceImpl;

    @Mock
    private Row row;

    @Mock
    private Sheet sheet;

    @Mock
    private Cell cell;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        excelUpdateServiceImpl = new ExcelUpdateServiceImpl();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheetName = workbook.createSheet("Sheet1");
    }


    @Test
    public void testReadDataFromSheet() throws IOException {
        // Create a sample Excel file with some data
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet1");
        sheet.createRow(0).createCell(0).setCellValue("Name");
        sheet.getRow(0).createCell(1).setCellValue("Age");
        sheet.createRow(1).createCell(0).setCellValue("Abhi");
        sheet.getRow(1).createCell(1).setCellValue("25");
        sheet.createRow(2).createCell(0).setCellValue("Raut");
        sheet.getRow(2).createCell(1).setCellValue("26");
        File tempFile = File.createTempFile("test", ".xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
        workbook.write(fileOutputStream);
        workbook.close();

        // Read data from the sheet using the method under test
        FileInputStream inputStream = new FileInputStream(tempFile);
        XSSFWorkbook workbook1 = new XSSFWorkbook(inputStream);
        XSSFSheet sheet1 = workbook1.getSheetAt(0);
        excelUpdateServiceImpl = new ExcelUpdateServiceImpl();
        List<List<Object>> excelData = excelUpdateServiceImpl.readDataFromSheet(sheet1);

//         Verify the result
        Assert.assertEquals(Arrays.asList("Name", "Age"), excelData.get(0));
        Assert.assertEquals(Arrays.asList("Abhi", "25"), excelData.get(1));
        Assert.assertEquals(Arrays.asList("Raut", "26"), excelData.get(2));

        // Clean up
        workbook1.close();
        inputStream.close();
        tempFile.delete();
    }

}
