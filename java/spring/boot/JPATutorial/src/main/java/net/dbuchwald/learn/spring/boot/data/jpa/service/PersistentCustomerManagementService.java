package net.dbuchwald.learn.spring.boot.data.jpa.service;

import net.dbuchwald.learn.spring.boot.data.jpa.entity.CustomerIdentifier;
import net.dbuchwald.learn.spring.boot.data.jpa.repository.CustomerIdentifierRepository;
import net.dbuchwald.learn.spring.boot.data.jpa.repository.CustomerRepository;
import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.Customer;
import net.dbuchwald.learn.spring.boot.data.jpa.repository.IdentifierTypeRepository;
import net.dbuchwald.learn.spring.boot.data.jpa.service.mapper.CustomerDTOMapperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PersistentCustomerManagementService implements CustomerManagementService {

  private final CustomerRepository customerRepository;

  private final CustomerIdentifierRepository customerIdentifierRepository;

  private final IdentifierTypeRepository identifierTypeRepository;

  private final CustomerDTOMapperService customerDTOMapperService;

  public PersistentCustomerManagementService(CustomerRepository customerRepository,
                                             CustomerIdentifierRepository customerIdentifierRepository,
                                             IdentifierTypeRepository identifierTypeRepository,
                                             CustomerDTOMapperService customerDTOMapperService) {
    this.customerRepository = customerRepository;
    this.customerIdentifierRepository = customerIdentifierRepository;
    this.identifierTypeRepository = identifierTypeRepository;
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
  public Optional<CustomerDTO> findCustomerByIdTypeAndValue(String country, String idType, String value) {
    return identifierTypeRepository.findByCountryAndIdType(country, idType)
        .map(identifierType -> customerIdentifierRepository.findByIdentifierTypeIdAndIdentifierValue(identifierType.getId(), value))
        .orElse(Optional.empty())
        .map(CustomerIdentifier::getCustomer)
        .map(customerDTOMapperService);
  }

  @Override
  public List<CustomerDTO> findAll() {
    return customerRepository.findAll().stream().map(customerDTOMapperService).collect(Collectors.toList());
  }
}
