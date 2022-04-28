package net.dbuchwald.k8s.apps.api.services;

import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.repositories.LanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LanguageService {

  @Autowired
  private LanguageRepository languageRepository;

  public List<Language> list() {
    return languageRepository.findAll();
  }
}
