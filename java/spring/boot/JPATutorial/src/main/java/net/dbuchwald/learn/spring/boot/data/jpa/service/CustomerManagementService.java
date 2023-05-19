package net.dbuchwald.learn.spring.boot.data.jpa.service;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.service.exception.CustomerCreationException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerManagementService {

  UUID createCustomer(String firstName, String lastName) throws CustomerCreationException;

  Optional<CustomerDTO> findCustomerById(UUID id);

  List<CustomerDTO> findCustomerByLastName(String lastName);

  List<CustomerDTO> findAll();
}
