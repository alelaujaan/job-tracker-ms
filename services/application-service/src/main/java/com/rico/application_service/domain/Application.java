package com.rico.application_service.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "applications")
public class Application {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String source; // INFOJOBS, LINKEDIN, MANUAL

  @Column(nullable = false, length = 2000)
  private String url;

  private String company;
  private String title;
  private String location;

  @Column(columnDefinition = "TEXT")
  private String description;

  private Integer salaryMin;
  private Integer salaryMax;
  private String currency;

  @Column(nullable = false)
  private String status; // APPLIED, HR, TECH, OFFER, REJECTED

  private LocalDate appliedAt;

  // Getters/Setters (o Lombok)
  public Long getId() { return id; }
  public void setId(Long id) { this.id = id; }

  public String getSource() { return source; }
  public void setSource(String source) { this.source = source; }

  public String getUrl() { return url; }
  public void setUrl(String url) { this.url = url; }

  public String getCompany() { return company; }
  public void setCompany(String company) { this.company = company; }

  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }

  public String getLocation() { return location; }
  public void setLocation(String location) { this.location = location; }

  public String getDescription() { return description; }
  public void setDescription(String description) { this.description = description; }

  public Integer getSalaryMin() { return salaryMin; }
  public void setSalaryMin(Integer salaryMin) { this.salaryMin = salaryMin; }

  public Integer getSalaryMax() { return salaryMax; }
  public void setSalaryMax(Integer salaryMax) { this.salaryMax = salaryMax; }

  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }

  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }

  public LocalDate getAppliedAt() { return appliedAt; }
  public void setAppliedAt(LocalDate appliedAt) { this.appliedAt = appliedAt; }
}
