from app.schemas import JobImportDTO

def scrape_linkedin(url: str) -> JobImportDTO:
    # Sin scraping agresivo: por ahora devolvemos lo básico
    return JobImportDTO(
        source="LINKEDIN",
        url=url,
        currency="EUR",
    )
