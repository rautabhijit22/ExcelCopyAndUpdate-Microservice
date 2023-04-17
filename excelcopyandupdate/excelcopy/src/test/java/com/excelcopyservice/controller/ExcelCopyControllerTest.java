package com.excelcopyservice.controller;

import com.excelcopyservice.exception.ExcelCopyException;
import com.excelcopyservice.service.ExcelCopyService;
import com.excelcopyservice.util.RestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class ExcelCopyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ExcelCopyService excelCopyService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private RestUtil restUtil;
    @InjectMocks
    private ExcelCopyController excelCopyController;

    @Value("${sheetName}")
    private String sheetName;

    @BeforeEach
    public void setup() {
        excelCopyController = new ExcelCopyController();
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(excelCopyController).build();

    }

    @Test
    void testCopyDataFromSheetToSheet_Success(){
        String sourcePath = "source";
        String destinationPath = "destination";

        doNothing().when(excelCopyService).copyDataFromSheetToSheet(sourcePath, destinationPath, sheetName);
        doNothing().when(restUtil).callToExcelUpdateService(destinationPath);

        ResponseEntity<String> response = excelCopyController.copyDataFromSheetToSheet(sourcePath, destinationPath, sheetName);

        verify(excelCopyService, times(1)).copyDataFromSheetToSheet(sourcePath, destinationPath, sheetName);
        verify(restUtil, times(1)).callToExcelUpdateService(destinationPath);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Data copied successfully!", response.getBody());
    }


    @Test
    void testCopyDataFromSheetToSheet_Failure(){
        String sourcePath = "source";
        String destinationPath = "destination";

        doThrow(new ExcelCopyException("Copy failed")).when(excelCopyService).copyDataFromSheetToSheet(sourcePath, destinationPath, "Sheet1");

        ExcelCopyException exception = assertThrows(ExcelCopyException.class,
                () -> excelCopyController.copyDataFromSheetToSheet(sourcePath, destinationPath,"Sheet1"));

        verify(excelCopyService, times(1)).copyDataFromSheetToSheet(sourcePath, destinationPath, "Sheet1");
        verify(restUtil, times(0)).callToExcelUpdateService(destinationPath);

        assertEquals("Copy failed", exception.getMessage());
    }
}