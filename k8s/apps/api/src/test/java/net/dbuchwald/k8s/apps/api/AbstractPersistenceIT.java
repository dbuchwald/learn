package net.dbuchwald.k8s.apps.api;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.GreetingId;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.models.constants.GreetingCode;
import net.dbuchwald.k8s.apps.api.models.constants.LanguageIdentifier;
import net.dbuchwald.k8s.apps.api.services.ReferenceDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
abstract class AbstractPersistenceIT {

  @Autowired
  ReferenceDataService referenceDataService;

  @Test
  @DisplayName("All entries in LANGUAGES table should be loaded correctly")
  void languagesListIsLoaded() {
    List<Language> languageList = referenceDataService.getAllLanguages();
    assert(languageList).contains(Language.of(LanguageIdentifier.POLISH, "Polski"));
    assert(languageList).contains(Language.of(LanguageIdentifier.ENGLISH, "English"));
    assert(languageList).contains(Language.of(LanguageIdentifier.SPANISH, "Español"));
  }

  @Test
  @DisplayName("Specific entry from LANGUAGES table should be loaded correctly")
  void languagesAreLoaded() {
    Optional<Language> polishLanguage = referenceDataService.getLanguage(LanguageIdentifier.POLISH);
    assertTrue(polishLanguage.isPresent());
    assertEquals("Polski", polishLanguage.get().getDescription());
  }

  @Test
  @DisplayName("Nonexistent entry from LANGUAGES table should not be loaded correctly")
  void invalidLanguagesAreNotLoaded() {
    Optional<Language> russianLanguage = referenceDataService.getLanguage("ru");
    assertTrue(russianLanguage.isEmpty());
  }

  @Test
  @DisplayName("Specific entry from GREETINGS table should be loaded correctly")
  void greetingsAreLoaded() {
    Optional<Greeting> englishGreeting = referenceDataService.getGreeting(LanguageIdentifier.ENGLISH, GreetingCode.HELLO);
    assertTrue(englishGreeting.isPresent());
    assertEquals(GreetingId.of(LanguageIdentifier.ENGLISH, GreetingCode.HELLO), englishGreeting.get().getGreetingId());
    assertEquals("Hello!", englishGreeting.get().getText());
  }

  @Test
  @DisplayName("Nonexistent entry from GREETINGS table should not be loaded correctly")
  void invalidGreetingsAreNotLoaded() {
    Optional<Greeting> russianGreeting = referenceDataService.getGreeting("ru", GreetingCode.HELLO);
    assertTrue(russianGreeting.isEmpty());
  }
}
