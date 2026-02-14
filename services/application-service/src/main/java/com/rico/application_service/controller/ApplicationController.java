package com.rico.application_service.controller;

import com.rico.application_service.domain.Application;
import org.springframework.web.bind.annotation.*;
import com.rico.application_service.repository.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

  private final ApplicationRepository repo;

  public ApplicationController(ApplicationRepository repo) {
    this.repo = repo;
  }

  @PostMapping
  public Application create(@RequestBody Application a) {
    a.setId(null);
    if (a.getStatus() == null) a.setStatus("APPLIED");
    if (a.getAppliedAt() == null) a.setAppliedAt(LocalDate.now());
    return repo.save(a);
  }

  @GetMapping
  public List<Application> list() {
    return repo.findAll();
  }

  @GetMapping("/{id}")
  public Application get(@PathVariable Long id) {
    return repo.findById(id).orElseThrow();
  }

  @PutMapping("/{id}")
  public Application update(@PathVariable Long id, @RequestBody Application a) {
    Application existing = repo.findById(id).orElseThrow();

    existing.setSource(a.getSource());
    existing.setUrl(a.getUrl());
    existing.setCompany(a.getCompany());
    existing.setTitle(a.getTitle());
    existing.setLocation(a.getLocation());
    existing.setDescription(a.getDescription());
    existing.setSalaryMin(a.getSalaryMin());
    existing.setSalaryMax(a.getSalaryMax());
    existing.setCurrency(a.getCurrency());
    existing.setStatus(a.getStatus());
    existing.setAppliedAt(a.getAppliedAt());

    return repo.save(existing);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    repo.deleteById(id);
  }
}
