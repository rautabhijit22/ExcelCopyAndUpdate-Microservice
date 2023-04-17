package com.excelcopyservice.service;

import com.excelcopyservice.exception.ExcelCopyException;
import com.excelcopyservice.util.RestUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Service
public class ExcelCopyServiceImpl implements ExcelCopyService {

    Logger logger = LoggerFactory.getLogger(ExcelCopyServiceImpl.class);

    @Autowired
    private RestUtil restUtil;
    @Value("${sheetName}")
    private String sheetName;

    public ExcelCopyServiceImpl() {
    }

    /**
     * Service class copy method holds actual logic to copy data from
     * source sheet to destination sheet
     */
    public void copyDataFromSheetToSheet(String sourcePath, String destinationPath, String sheetName) {
        try {
            // open source workbook
            InputStream sourceInputStream = new FileInputStream(sourcePath);
            XSSFWorkbook sourceWorkbook = new XSSFWorkbook(sourceInputStream);
            sourceInputStream.close();

            // get source sheet
            Sheet sourceSheet = sourceWorkbook.getSheet(sheetName);

            // open destination workbook
            InputStream destinationInputStream = new FileInputStream(destinationPath);
            XSSFWorkbook destinationWorkbook = new XSSFWorkbook(destinationInputStream);
            destinationInputStream.close();

            // get or create destination sheet
            Sheet destinationSheet = destinationWorkbook.getSheet(sheetName);
            if (destinationSheet == null) {
                destinationSheet = destinationWorkbook.createSheet(sheetName);
            }

            copyDataFromSourceToDestSheet(sourceSheet, destinationSheet);

            // save changes to destination workbook
            FileOutputStream outputStream = new FileOutputStream(destinationPath);
            destinationWorkbook.write(outputStream);
            outputStream.close();

            // close workbooks
            sourceWorkbook.close();
            destinationWorkbook.close();
        } catch (IOException e) {
            logger.info("Error received in service layer while copying data ");
            throw new ExcelCopyException("Error copying data: ", e);
        }

    }

    private void copyDataFromSourceToDestSheet(Sheet sourceSheet, Sheet destinationSheet) {
        for (Row sourceRow : sourceSheet) {
            Row destinationRow = destinationSheet.getRow(sourceRow.getRowNum());
            if (destinationRow == null) {
                destinationRow = destinationSheet.createRow(sourceRow.getRowNum());
            }

            for (Cell sourceCell : sourceRow) {
                Cell destinationCell = destinationRow.getCell(sourceCell.getColumnIndex());
                if (destinationCell == null) {
                    destinationCell = destinationRow.createCell(sourceCell.getColumnIndex());
                }
                destinationCell.setCellValue(sourceCell.toString());
            }
        }
    }
}
