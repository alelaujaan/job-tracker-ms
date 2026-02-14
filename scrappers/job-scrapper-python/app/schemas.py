from pydantic import BaseModel
from typing import Optional

class ScrapeRequest(BaseModel):
    url: str

class JobImportDTO(BaseModel):
    source: str
    url: str
    company: Optional[str] = None
    title: Optional[str] = None
    location: Optional[str] = None
    description: Optional[str] = None
    salaryMin: Optional[int] = None
    salaryMax: Optional[int] = None
    currency: Optional[str] = "EUR"
