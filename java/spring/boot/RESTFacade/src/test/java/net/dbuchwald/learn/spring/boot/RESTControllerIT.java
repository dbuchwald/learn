package net.dbuchwald.learn.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RESTControllerIT {

  @Autowired
  private TestRestTemplate template;

  @Test
  @DisplayName("REST interface for /greeting/en should work correctly")
  public void getGreetingEnShouldWorkCorrectly() {
    ResponseEntity<String> response = template.getForEntity("/greeting/en", String.class);
    assertThat(response.getBody()).isEqualTo("Hello World!");
  }

  @Test
  @DisplayName("REST JSON interface for /greeting/en should work correctly")
  public void getGreetingEnJsonShouldWorkCorrectly() {
    ResponseEntity<Greeting> response = template.getForEntity("/greeting/en", Greeting.class);
    assertThat(response.getBody().getGreeting()).isEqualTo("Hello World!");
    assertThat(response.getBody().getLanguage()).isEqualTo("en");
  }
}
