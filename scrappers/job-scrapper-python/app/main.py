from fastapi import FastAPI
from urllib.parse import urlparse

from app.schemas import ScrapeRequest, JobImportDTO
from app.providers.infojobs import scrape_infojobs
from app.providers.linkedin import scrape_linkedin

app = FastAPI(title="Job Scraper Service")

@app.get("/health")
def health():
    return {"status": "ok"}

@app.post("/scrape", response_model=JobImportDTO)
def scrape(req: ScrapeRequest):
    url = req.url.strip()
    host = urlparse(url).netloc.lower()

    if "infojobs" in host:
        return scrape_infojobs(url)
    if "linkedin" in host:
        return scrape_linkedin(url)

    return JobImportDTO(source="UNKNOWN", url=url)
