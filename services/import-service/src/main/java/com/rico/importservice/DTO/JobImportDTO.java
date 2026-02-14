package com.rico.importservice.DTO;

public record JobImportDTO(
        String source,
        String url,
        String company,
        String title,
        String location,
        String description,
        Integer salaryMin,
        Integer salaryMax,
        String currency
) {}
