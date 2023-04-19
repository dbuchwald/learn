package net.dbuchwald.learn.ssl.client;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class SslClientApplicationTests {

	@MockBean
	RestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

}
