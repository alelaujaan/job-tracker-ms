package com.rico.importservice.controller;

import com.rico.importservice.DTO.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/import")
public class ImportController {

    private final RestClient restClient;
    private final String scraperBaseUrl;
    private final String applicationsBaseUrl;

    public ImportController(
            @Value("${scraper.base-url}") String scraperBaseUrl,
            @Value("${applications.base-url}") String applicationsBaseUrl
    ) {
        this.restClient = RestClient.create();
        this.scraperBaseUrl = scraperBaseUrl;
        this.applicationsBaseUrl = applicationsBaseUrl;
    }

    @PostMapping
    public Object importJob(@RequestBody ScrapeRequest request) {

        // 1️⃣ Llamar al scraper
        JobImportDTO scraped = restClient.post()
                .uri(scraperBaseUrl + "/scrape")
                .body(request)
                .retrieve()
                .body(JobImportDTO.class);

        // 2️⃣ Crear candidatura en applications-service
        return restClient.post()
                .uri(applicationsBaseUrl + "/applications")
                .body(scraped)
                .retrieve()
                .body(Object.class);
    }
}
