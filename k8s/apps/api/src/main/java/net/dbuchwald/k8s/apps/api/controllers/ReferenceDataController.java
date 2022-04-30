package net.dbuchwald.k8s.apps.api.controllers;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.services.ReferenceDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ref-data")
public class ReferenceDataController {

  @Autowired
  ReferenceDataService referenceDataService;

  public ReferenceDataController(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @GetMapping(path = "/languages", produces = "application/json")
  public List<Language> getLanguagesList() {
    return referenceDataService.getAllLanguages();
  }

  @GetMapping(path = "/language/{langId}", produces = "application/json")
  public Language getLanguage(@PathVariable String langId) {
    return referenceDataService.getLanguage(langId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language not found"));
  }

  @GetMapping(path = "/greeting/{langId}/{code}", produces = "application/json")
  public Greeting getGreeting(@PathVariable String langId, @PathVariable String code) {
    return referenceDataService.getGreeting(langId, code)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Greeting not found"));
  }
}
