package net.dbuchwald.learn.ssl.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @SuppressWarnings("SameReturnValue")
  @GetMapping("/hello")
  public String hello() {
    return "Hello World!";
  }
}
