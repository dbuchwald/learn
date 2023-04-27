package net.dbuchwald.learn.ssl.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class SslClientApplicationTest {

  @MockBean
  CommandLineRunner mockCLR;

  @Test
  void contextLoads() {
  }

}
