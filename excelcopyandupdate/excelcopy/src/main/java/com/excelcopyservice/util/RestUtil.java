package com.excelcopyservice.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestUtil {

    @Value("${api.url}")
    private String apiUrl;

    @Autowired
    RestTemplate restTemplate;
    public void callToExcelUpdateService(String destinationPath) {
        String updateServiceURL = apiUrl + "?filePath=" + destinationPath;
        restTemplate.postForObject(updateServiceURL, null, String.class);
    }

}
