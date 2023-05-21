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

import java.util.List;
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

			List<CustomerDTO> allCustomers = customerManagementService.findAll();

			String sampleID = allCustomers.get(0).getId();

			for (CustomerDTO customerDTO : allCustomers) {
				log.info(customerDTO.toString());
			}

			log.info("Find by id");

			Optional<CustomerDTO> optionalCustomerDTO = customerManagementService.findCustomerById(UUID.fromString("b09f4c5c-45b0-48aa-a018-695914d20555"));
			log.info(optionalCustomerDTO.map(CustomerDTO::toString).orElse("Customer with incorrect uuid not found"));
			optionalCustomerDTO = customerManagementService.findCustomerById(UUID.fromString(sampleID));
			log.info(optionalCustomerDTO.map(CustomerDTO::toString).orElse("Customer with " + sampleID + " not found"));

			log.info("find by last name");

			for (CustomerDTO customerDTO : customerManagementService.findCustomerByLastName("Smith")) {
				log.info(customerDTO.toString());
			}

			for (IdentifierTypeDTO identifierTypeDTO : identifierTypeManagementService.findAll()) {
				log.info(identifierTypeDTO.toString());
			}

			log.info("That's it");
		};
	}
}
