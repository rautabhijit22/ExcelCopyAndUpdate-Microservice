package com.excelupdateservice.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.excelupdateservice.exception.ExcelUpdateException;
import com.excelupdateservice.service.ExcelUpdateService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ExcelUpdateControllerTest {

    @Mock
    private ExcelUpdateService excelUpdateService;

    private ExcelUpdateController excelUpdateController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        excelUpdateController = new ExcelUpdateController(excelUpdateService);
    }

    @Test
    public void testHandleExcelUpdateEvent() throws IOException, ExcelUpdateException {
        String excelPath = "C:/Users/User/Downloads/infy.xlsx";
        String sheetName = "Sheet1";

        // Mocking the ExcelUpdateService
        XSSFWorkbook workbook = mock(XSSFWorkbook.class);
        XSSFSheet sheet = mock(XSSFSheet.class);
        List<List<Object>> dataFromSheet = new ArrayList<List<Object>>();
        when(workbook.getSheet(sheetName)).thenReturn(sheet);
        when(excelUpdateService.readDataFromSheet(any())).thenReturn(dataFromSheet);

        // Calling the method being tested
        ResponseEntity<String> responseEntity = excelUpdateController.handleExcelUpdateEvent(excelPath);

        // Verifying the results
        Assert.assertEquals("Excel file updated successfully", responseEntity.getBody());
    }

}
