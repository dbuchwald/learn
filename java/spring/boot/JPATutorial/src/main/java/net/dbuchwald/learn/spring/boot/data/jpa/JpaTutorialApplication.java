package net.dbuchwald.learn.spring.boot.data.jpa;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.dto.IdentifierTypeDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.service.CustomerManagementService;
import net.dbuchwald.learn.spring.boot.data.jpa.service.IdentifierTypeManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class JpaTutorialApplication {

	private static final Logger log = LoggerFactory.getLogger(JpaTutorialApplication.class);

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(SpringApplication.run(JpaTutorialApplication.class, args)));
	}

	@Bean
	public CommandLineRunner run(CustomerManagementService customerManagementService,
															 IdentifierTypeManagementService identifierTypeManagementService) {
		return (args) -> {
			customerManagementService.createCustomer("Dawid", "Buchwald");
			UUID irenaUUID = customerManagementService.createCustomer("Irena", "Buchwald");
			customerManagementService.createCustomer("Wiktor", "Buchwald");
			customerManagementService.createCustomer("Kuba", "Latecki");

			log.info("Customers created");

			for (CustomerDTO customerDTO : customerManagementService.findAll()) {
				log.info(customerDTO.toString());
			}

			log.info("Find by id");

			Optional<CustomerDTO> optionalCustomerDTO = customerManagementService.findCustomerById(UUID.fromString("b09f4c5c-45b0-48aa-a018-695914d20555"));
			log.info(optionalCustomerDTO.map(CustomerDTO::toString).orElse("Customer with incorrect uuid not found"));
			optionalCustomerDTO = customerManagementService.findCustomerById(irenaUUID);
			log.info(optionalCustomerDTO.map(CustomerDTO::toString).orElse("Customer with " + irenaUUID + " not found"));

			log.info("find by last name");

			for (CustomerDTO customerDTO : customerManagementService.findCustomerByLastName("Buchwald")) {
				log.info(customerDTO.toString());
			}

			for (IdentifierTypeDTO identifierTypeDTO : identifierTypeManagementService.findAll()) {
				log.info(identifierTypeDTO.toString());
			}

			log.info("That's it");
		};
	}

}
