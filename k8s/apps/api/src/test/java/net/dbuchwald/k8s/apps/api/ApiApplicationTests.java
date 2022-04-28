package net.dbuchwald.k8s.apps.api;

import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.services.LanguageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ApiApplicationTests {

	@Autowired
	LanguageService languageService;

	@Test
	void contextLoads() {
	}

	@Test
	void referenceDataIsLoaded() {
		List<Language> languageList = languageService.list();
		assert(languageList).contains(new Language("pl", "Polski"));
		assert(languageList).contains(new Language("en", "English"));
	}
}
