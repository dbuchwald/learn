package net.dbuchwald.learn.spring.boot;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RESTControllerMockIT {

  @Autowired
  private MockMvc mvc;

  @Test
  @DisplayName("REST endpoint for /greeting/en should return correct message")
  void getGreetingEnShouldWorkCorrectly () throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/greeting/en").accept(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("Hello World!")));
  }

  @Test
  @DisplayName("REST endpoint for /greeting/es should return correct message")
  void getGreetingEsShouldWorkCorrectly () throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/greeting/es").accept(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk())
        .andExpect(content().string(equalTo("Hola Mundo!")));
  }

  @Test
  @DisplayName("REST endpoint for /greeting/en should return correct message")
  void getGreetingEnJsonShouldWorkCorrectly () throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/greeting/en").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"language\"=\"en\", \"greeting\"=\"Hello World!\"}"));
  }

  @Test
  @DisplayName("REST endpoint for /greeting/es should return correct message")
  void getGreetingEsJsonShouldWorkCorrectly () throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/greeting/es").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("{\"language\"=\"es\", \"greeting\"=\"Hola Mundo!\"}"));
  }

}
