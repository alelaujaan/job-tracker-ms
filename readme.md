# 🚀 Job Tracker Microservices

Plataforma modular para gestionar y monitorizar candidaturas de empleo mediante arquitectura de microservicios.

La aplicación permite importar ofertas desde portales externos, normalizar los datos y almacenarlos para su seguimiento desde una interfaz web o aplicación de escritorio.

---

## 🧱 Arquitectura

El sistema está diseñado bajo un enfoque orientado a microservicios, donde cada componente tiene una responsabilidad clara y desacoplada.

Desktop App (JavaFX)
│
▼
Angular Web UI
│
▼
Import Service (Spring Boot)
│
▼
Job Scraper Service (Python + FastAPI)
│
▼
Applications Service (Spring Boot)
│
▼
PostgreSQL



### Principios aplicados

- Separación de responsabilidades  
- Comunicación HTTP entre servicios  
- Contenerización completa  
- Escalabilidad independiente por servicio  
- Arquitectura desacoplada  

---

## 📦 Servicios

### 🔹 Applications Service (Spring Boot)

- API REST `/applications`  
- CRUD completo de candidaturas  
- Persistencia en PostgreSQL  
- Normalización de estados y datos  

Puerto: **8081**

---

### 🔹 Import Service (Spring Boot)

- Endpoint `/import`  
- Orquesta la importación de ofertas  
- Llama al scraper  
- Guarda el resultado en Applications Service  

Puerto: **8082**

---

### 🔹 Job Scraper Service (Python + FastAPI)

- Endpoint `/scrape`  
- Extrae información estructurada desde URLs externas  
- Devuelve un DTO normalizado  

Puerto: **8000**

⚠️ Actualmente se está trabajando en la implementación completa y robusta del scraping de:
- InfoJobs  
- LinkedIn  

---

### 🔹 Angular Web UI

- Importación de ofertas por URL  
- Listado y visualización de candidaturas  
- Interfaz moderna y desacoplada del backend  

Puerto: **4200**

---

### 🔹 Desktop Application (JavaFX)

- Cliente de escritorio  
- Permite importar y listar candidaturas  
- Puede lanzar automáticamente la versión web  

---

## 🐳 Docker

El sistema completo puede ejecutarse mediante Docker Compose.

### Ejecutar todo el stack

```bash
docker compose up -d --build
