package net.dbuchwald.learn.spring.boot;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"default", "DEV"})
public class GreetingServiceSimpleImpl implements GreetingService {
  @Override
  public String getGreeting(String language) {
    if (language == null) {
      return null;
    }
    switch (language.toUpperCase()) {
      case "EN":
        return "Hello World!";
      case "ES":
        return "Hola Mundo!";
      case "PL":
        return "Witaj Świecie!";
      default:
        return null;
    }
  }
}
