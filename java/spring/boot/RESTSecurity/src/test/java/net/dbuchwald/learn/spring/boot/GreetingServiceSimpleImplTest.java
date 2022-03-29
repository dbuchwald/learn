package net.dbuchwald.learn.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class GreetingServiceSimpleImplTest {

  GreetingServiceSimpleImpl simpleGreetingService = new GreetingServiceSimpleImpl();

  @Test
  @DisplayName("getGreeting should return correct value")
  void getGreeting() {
    assertThat(simpleGreetingService.getGreeting("en")).isEqualTo("Hello World!");
    assertThat(simpleGreetingService.getGreeting("PL")).isEqualTo("Witaj Świecie!");
    assertThat(simpleGreetingService.getGreeting("ES")).isEqualTo("Hola Mundo!");
    assertThat(simpleGreetingService.getGreeting("NoSuchLanguage")).isNull();
    assertThat(simpleGreetingService.getGreeting(null)).isNull();
  }
}