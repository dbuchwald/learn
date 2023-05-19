package net.dbuchwald.learn.spring.boot.data.jpa.service;

import net.dbuchwald.learn.spring.boot.data.jpa.repository.CustomerRepository;
import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.Customer;
import net.dbuchwald.learn.spring.boot.data.jpa.service.mapper.CustomerDTOMapperService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PersistentCustomerManagementService implements CustomerManagementService {

  private final CustomerRepository customerRepository;

  private final CustomerDTOMapperService customerDTOMapperService;

  public PersistentCustomerManagementService(CustomerRepository customerRepository,
                                             CustomerDTOMapperService customerDTOMapperService) {
    this.customerRepository = customerRepository;
    this.customerDTOMapperService = customerDTOMapperService;
  }

  @Override
  public UUID createCustomer(String firstName, String lastName)  {
    Customer customer = new Customer(firstName, lastName);
    customerRepository.save(customer);
    return customer.getId();
  }

  @Override
  public Optional<CustomerDTO> findCustomerById(UUID id) {
    return customerRepository.findById(id).map(customerDTOMapperService);
  }

  @Override
  public List<CustomerDTO> findCustomerByLastName(String lastName) {
    return customerRepository.findByLastName(lastName).stream().map(customerDTOMapperService).collect(Collectors.toList());
  }

  @Override
  public List<CustomerDTO> findAll() {
    return customerRepository.findAll().stream().map(customerDTOMapperService).collect(Collectors.toList());
  }
}
