package net.dbuchwald.k8s.apps.api;

import net.dbuchwald.k8s.apps.api.models.Greeting;
import net.dbuchwald.k8s.apps.api.models.Language;
import net.dbuchwald.k8s.apps.api.models.constants.GreetingCode;
import net.dbuchwald.k8s.apps.api.models.constants.LanguageIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiV1RefDataIT {

  private static final String REF_DATA_API_V1 = "/api/v1/ref-data";

  @Autowired
  private TestRestTemplate template;

  @Test
  @DisplayName("REST interface for /languages should work correctly")
  public void getLanguagesListShouldWorkCorrectly() {
    ResponseEntity<Language[]> response = template.getForEntity(REF_DATA_API_V1 + "/languages", Language[].class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    Language[] result = response.getBody();
    assertThat(result).containsOnly(Language.of(LanguageIdentifier.POLISH, "Polski"),
                                    Language.of(LanguageIdentifier.ENGLISH, "English"),
                                    Language.of(LanguageIdentifier.SPANISH, "Español"));
  }

  @Test
  @DisplayName("REST interface for /language/langId should work correctly")
  public void getLanguageShouldWorkCorrectly() {
    ResponseEntity<Language> response = template.getForEntity(REF_DATA_API_V1 + "/language/" + LanguageIdentifier.ENGLISH,
        Language.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isEqualTo(Language.of(LanguageIdentifier.ENGLISH, "English"));
  }

  @Test
  @DisplayName("REST interface for /language/langId should return BAD REQUEST for invalid value")
  public void getLanguageShouldWorkCorrectlyForInvalidValue() {
    ResponseEntity<Language> response = template.getForEntity(REF_DATA_API_V1 + "/language/ru", Language.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  @Test
  @DisplayName("REST interface for /greeting/langId/code should work correctly")
  public void getGreetingShouldWorkCorrectly() {
    ResponseEntity<Greeting> response = template.getForEntity(REF_DATA_API_V1 + "/greeting/" + LanguageIdentifier.SPANISH +
            "/" + GreetingCode.HELLO, Greeting.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    Greeting result = response.getBody();
    assertThat(result.getGreetingId().getLangId()).isEqualTo(LanguageIdentifier.SPANISH);
    assertThat(result.getGreetingId().getCode()).isEqualTo(GreetingCode.HELLO);
    assertThat(result.getText()).isEqualTo("Hola!");
  }

  @Test
  @DisplayName("REST interface for /greeting/langId/code should return NOT FOUND for invalid value")
  public void getGreetingShouldWorkCorrectlyForInvalidValue() {
    ResponseEntity<Greeting> response = template.getForEntity(REF_DATA_API_V1 + "/greeting/ru/hello", Greeting.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }
}
