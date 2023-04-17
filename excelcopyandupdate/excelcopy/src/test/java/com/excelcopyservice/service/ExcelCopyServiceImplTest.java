package com.excelcopyservice.service;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ExcelCopyServiceImplTest {

    @InjectMocks
    private ExcelCopyServiceImpl excelCopyService;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testCopyDataFromSheetToSheet() throws Exception {
        // arrange
        String sourcePath = "C:/Users/User/Downloads/infy.xlsx";
        String destinationPath = "C:/Users/User/Downloads/Reliance.xlsx";

        InputStream sourceInputStream = new FileInputStream(sourcePath);
        XSSFWorkbook sourceWorkbook = new XSSFWorkbook(sourceInputStream);
        sourceInputStream.close();

        Sheet sourceSheet = sourceWorkbook.getSheet("Sheet1");

        XSSFWorkbook destinationWorkbook = mock(XSSFWorkbook.class);
        when(destinationWorkbook.getSheet(any(String.class))).thenReturn(null);
        when(destinationWorkbook.createSheet(any(String.class))).thenReturn(mock(XSSFSheet.class));

        // act
        excelCopyService.copyDataFromSheetToSheet(sourcePath, destinationPath, "Sheet1");

        // assert
        InputStream destinationInputStream = new FileInputStream(destinationPath);
        XSSFWorkbook resultWorkbook = new XSSFWorkbook(destinationInputStream);
        Sheet resultSheet = resultWorkbook.getSheet("Sheet1");

        assertEquals(sourceSheet.getLastRowNum(), resultSheet.getLastRowNum());
        for (int i = 0; i <= sourceSheet.getLastRowNum(); i++) {
            if (sourceSheet.getRow(i) != null) {
                Row expectedRow = sourceSheet.getRow(i);
                Row resultRow = resultSheet.getRow(i);
                assertNotNull(resultRow);
                assertEquals(expectedRow.getLastCellNum(), resultRow.getLastCellNum());
                for (int j = 0; j < expectedRow.getLastCellNum(); j++) {
                    assertEquals(expectedRow.getCell(j).toString(), resultRow.getCell(j).toString());
                }
            }
        }

        resultWorkbook.close();
        destinationInputStream.close();
    }

}
