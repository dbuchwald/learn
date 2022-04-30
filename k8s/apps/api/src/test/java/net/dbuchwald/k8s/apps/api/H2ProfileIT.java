package net.dbuchwald.k8s.apps.api;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.services.ReferenceDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("h2")
public class H2ProfileIT {
  @Autowired
  ReferenceDataService referenceDataService;

  @Test
  void languagesListIsLoaded() {
    List<Language> languageList = referenceDataService.getAllLanguages();
    assert(languageList).contains(new Language("pl", "Polski"));
    assert(languageList).contains(new Language("en", "English"));
  }

  @Test
  void languagesAreLoaded() {
    Optional<Language> polishLanguage = referenceDataService.getLanguage("pl");
    assertTrue(polishLanguage.isPresent());
    assertEquals("Polski", polishLanguage.get().getDescription());
  }

  @Test
  void greetingsAreLoaded() {
    Optional<Greeting> englishGreeting = referenceDataService.getGreeting("en", "hello");
    assertTrue(englishGreeting.isPresent());
    assertEquals("Hello!", englishGreeting.get().getText());
  }
}
