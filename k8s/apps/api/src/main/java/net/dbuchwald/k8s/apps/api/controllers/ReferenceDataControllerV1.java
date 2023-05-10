package net.dbuchwald.k8s.apps.api.controllers;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.services.ReferenceDataService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(ReferenceDataControllerV1.REF_DATA_API_V1_ROOT_PATH)
public class ReferenceDataControllerV1 {

  public static final String REF_DATA_API_V1_ROOT_PATH = "/api/v1/ref-data";

  private final ReferenceDataService referenceDataService;

  public ReferenceDataControllerV1(ReferenceDataService referenceDataService) {
    this.referenceDataService = referenceDataService;
  }

  @GetMapping(path = "/languages", produces = APPLICATION_JSON_VALUE)
  public List<Language> getLanguagesList() {
    return referenceDataService.getAllLanguages();
  }

  @GetMapping(path = "/language/{langId}", produces = APPLICATION_JSON_VALUE)
  public Language getLanguage(@PathVariable String langId) {
    return referenceDataService.getLanguage(langId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Language not found"));
  }

  @GetMapping(path = "/greeting/{langId}/{code}", produces = APPLICATION_JSON_VALUE)
  public Greeting getGreeting(@PathVariable String langId, @PathVariable String code) {
    return referenceDataService.getGreeting(langId, code)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Greeting not found"));
  }
}
