package net.dbuchwald.learn.ssl.client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GreetingClientService {

  @Value("${greeting.service.url}")
  private String greetingServiceUrl;

  private final RestTemplate restTemplate;

  public GreetingClientService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String getGreetingMessage() {
    return restTemplate.getForObject(greetingServiceUrl, String.class);
  }
}
