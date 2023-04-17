package com.excelupdateservice.service;

import com.excelupdateservice.exception.ExcelUpdateException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelUpdateServiceImpl implements ExcelUpdateService {

    Logger logger = LoggerFactory.getLogger(ExcelUpdateServiceImpl.class);

    /**
     * @param sheet received from controller method for reading purpose and copying in temp List
     * @return temp List<List<Object>>> excel data to controler method
     */
    public List<List<Object>> readDataFromSheet(XSSFSheet sheet) {

        List<List<Object>> excelData = new ArrayList<>();

        for (Row row : sheet) {
            Iterator<Cell> cellIterator = row.cellIterator();
            List<Object> rowData = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (cell.getCellType() == CellType.BLANK) {
                    break;
                }
                rowData.add(cell.toString());
            }
            excelData.add(rowData);
        }
        logger.info("excel file reading is completed");
        return excelData;
    }

    /**
     * @param dataFromSheet received from reading method present in service class
     * @param sheet1        destination sheet which needs to be updated with copied data present in dataFromSheet
     *                      return void as its just updating method handling copying task
     */
    public void updateSheetWithCopiedData(List<List<Object>> dataFromSheet, String sheet1, FileOutputStream fileOutputStream) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet(sheet1);

            int rowIndex = 0;
            for (List<Object> rowData : dataFromSheet) {
                Row row = sheet.createRow(rowIndex++);

                int cellIndex = 0;
                for (Object cellData : rowData) {
                    Cell cell = row.createCell(cellIndex++);
                    cell.setCellValue(cellData.toString());
                }
            }
            logger.info("updates in sheet completed");
            workbook.write(fileOutputStream);
            workbook.close();
        } catch (IOException e) {
            logger.info("Error received in ExcelupdateServiceImpl class while updating excel");
            throw new ExcelUpdateException("Error updating excel", e);
        }
    }
}
