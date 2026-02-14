import requests
from bs4 import BeautifulSoup
from urllib.parse import urlsplit, urlunsplit
from app.schemas import JobImportDTO

def _text(el):
    return el.get_text(strip=True) if el else None

def _clean_url(url: str) -> str:
    # Quita querystring (applicationOrigin, searchId, etc.)
    parts = urlsplit(url)
    return urlunsplit((parts.scheme, parts.netloc, parts.path, "", ""))

def scrape_infojobs(url: str) -> JobImportDTO:
    clean = _clean_url(url)

    headers = {
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 "
                      "(KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36",
        "Accept-Language": "es-ES,es;q=0.9,en;q=0.8",
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
        "Referer": "https://www.infojobs.net/",
        "Connection": "keep-alive",
    }

    session = requests.Session()

    # 1) Intento normal con URL limpia
    r = session.get(clean, headers=headers, timeout=20, allow_redirects=True)

    # 2) Si por alguna razón devuelve 405, intenta URL original limpia de redirects
    if r.status_code == 405:
        # A veces un HEAD te da Location correcto
        h = session.head(clean, headers=headers, timeout=20, allow_redirects=False)
        loc = h.headers.get("Location")
        if loc:
            if loc.startswith("//"):
                loc = "https:" + loc
            elif loc.startswith("/"):
                loc = "https://www.infojobs.net" + loc
            r = session.get(loc, headers=headers, timeout=20, allow_redirects=True)

    if r.status_code != 200:
        return JobImportDTO(source="INFOJOBS", url=url, description=f"HTTP {r.status_code}")

    soup = BeautifulSoup(r.text, "lxml")

    # Título
    title_el = soup.select_one("h1.ij-Heading-title1") or soup.select_one("h1")
    title = _text(title_el)

    # Empresa
    company_el = soup.select_one("a.ij-Heading-headline2.ij-BaseTypography-primary") \
                 or soup.select_one("a.ij-Heading-headline2")
    company = _text(company_el)

    # Descripción (fallback genérico; lo afinamos con snippet si hace falta)
    description = None
    for sel in [
        "[data-test='offer-description']",
        ".ij-OfferDetail-description",
        ".ij-OfferDetail-body",
        "main"
    ]:
        el = soup.select_one(sel)
        if el:
            text = el.get_text("\n", strip=True)
            if text and len(text) > 80:
                description = text
                break

    # Si no saca datos, guarda HTML para inspección
    if not title and not company and not description:
        with open("debug_infojobs.html", "w", encoding="utf-8") as f:
            f.write(r.text)
        return JobImportDTO(
            source="INFOJOBS",
            url=url,
            description="No pude extraer datos. Guardado debug_infojobs.html para ajustar selectores."
        )

    return JobImportDTO(
        source="INFOJOBS",
        url=url,
        company=company,
        title=title,
        location=None,
        description=description,
        salaryMin=None,
        salaryMax=None,
        currency="EUR"
    )
