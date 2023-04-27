package net.dbuchwald.learn.ssl.client.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(GreetingClientService.class)
class GreetingClientServiceTest {

	private final static String TEST_GREETING = "Test Greeting!";

	@Value("${greeting.service.url}")
	private String greetingServiceUrl;

	@Autowired
	GreetingClientService greetingClientService;

	@Autowired
	private MockRestServiceServer mockServer;

	@Test
	void greetingShouldBeRequested() throws URISyntaxException {
		mockServer.expect(ExpectedCount.once(), requestTo(new URI(greetingServiceUrl)))
				.andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).body(TEST_GREETING));

		String message = greetingClientService.getGreetingMessage();

		//mockServer.verify();
		assertEquals(TEST_GREETING, message);
	}
}
