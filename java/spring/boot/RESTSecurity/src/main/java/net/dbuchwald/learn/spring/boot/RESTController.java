package net.dbuchwald.learn.spring.boot;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

  @Autowired
  GreetingService greetingService;

  public RESTController(GreetingService service) {
    greetingService = service;
  }

  @Operation(summary = "Get plain language-specific greeting")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Greeting found for given language",
          content = { @Content(mediaType = "text/plain"), @Content(mediaType = "application/json") }),
      @ApiResponse(responseCode = "404", description = "Greeting not found for given language",
          content = @Content) })

  @GetMapping(path = "/greeting/{language}", produces = "text/plain")
  public String getGreeting(@Parameter(description = "Language identifier (en/es/pl/...)", example = "en")
                            @PathVariable String language) {
    return greetingService.getGreeting(language);
  }

  @GetMapping(path = "/greeting/{language}", produces = "application/json")
  public Greeting getJsonGreeting(@Parameter(description = "Language identifier (en/es/pl/...)", example = "es")
                                  @PathVariable String language) {
    return new Greeting(language, greetingService.getGreeting(language));
  }
}
