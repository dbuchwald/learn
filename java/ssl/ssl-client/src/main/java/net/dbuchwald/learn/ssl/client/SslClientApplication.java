package net.dbuchwald.learn.ssl.client;

import net.dbuchwald.learn.ssl.client.service.GreetingClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SslClientApplication implements CommandLineRunner {

	private final Logger logger = LoggerFactory.getLogger(SslClientApplication.class);

	private final GreetingClientService greetingClientService;

	public SslClientApplication(GreetingClientService greetingClientService) {
		this.greetingClientService = greetingClientService;
	}

	public static void main(String[] args) {
		SpringApplication.run(SslClientApplication.class, args);
	}

	@Override
	public void run(String... args) {
		logger.info("Retrieving greeting from service...");
		String greeting = greetingClientService.getGreetingMessage();
		logger.info("...done. Got the following message: [{}]", greeting);
	}
}
