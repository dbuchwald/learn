package net.dbuchwald.k8s.apps.api.services;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.GreetingId;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.repositories.GreetingRepository;
import net.dbuchwald.k8s.apps.api.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReferenceDataService {

  @Autowired
  private LanguageRepository languageRepository;

  @Autowired
  private GreetingRepository greetingRepository;

  public List<Language> getAllLanguages() {
    return languageRepository.findAll();
  }

  public Optional<Language> getLanguage(String langId) {
    return languageRepository.findById(langId);
  }

  public Optional<Greeting> getGreeting(String langId, String code) {
    return greetingRepository.findById(new GreetingId(langId, code));
  }
}
