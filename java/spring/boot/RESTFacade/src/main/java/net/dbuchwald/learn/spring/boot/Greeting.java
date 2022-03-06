package net.dbuchwald.learn.spring.boot;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Greeting {
  private final String language;
  private final String greeting;

  public Greeting(@JsonProperty("language") String language, @JsonProperty("greeting") String greeting) {
    this.language = language;
    this.greeting = greeting;
  }

  public String getGreeting() {
    return greeting;
  }

  public String getLanguage() {
    return language;
  }
}
