package com.excelcopyservice.controller;

import com.excelcopyservice.exception.ExcelCopyException;
import com.excelcopyservice.service.ExcelCopyService;
import com.excelcopyservice.util.RestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/excel")
public class ExcelCopyController {
    Logger logger = LoggerFactory.getLogger(ExcelCopyController.class);
    private ExcelCopyService excelCopyService;

    @Value("${sheetName}")
    private String sheetName;
    private RestUtil restUtil;

    @Autowired
    public ExcelCopyController(ExcelCopyService excelCopyService, RestUtil restUtil) {
        this.excelCopyService = excelCopyService;
        this.restUtil = restUtil;
    }


    public ExcelCopyController() {
    }

    /**
     * @param sourcePath      - source excel file path
     * @param destinationPath - destination excel file path
     * @return ResponseEntity message if successfully able to copy data
     * @throws ExcelCopyException error if failing to copy data
     */
    @PostMapping("/copy")
    public ResponseEntity<String> copyDataFromSheetToSheet(@RequestParam("source") String sourcePath,
                                                           @RequestParam("destination") String destinationPath, String sheetName) throws ExcelCopyException {

        excelCopyService.copyDataFromSheetToSheet(sourcePath, destinationPath, sheetName);
        logger.info("Recieved request for copying file");

        restUtil.callToExcelUpdateService(destinationPath);

        return ResponseEntity.ok("Data copied successfully!");
    }

}
