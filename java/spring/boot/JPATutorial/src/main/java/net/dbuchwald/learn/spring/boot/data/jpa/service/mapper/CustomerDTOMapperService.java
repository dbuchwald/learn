package net.dbuchwald.learn.spring.boot.data.jpa.service.mapper;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CustomerDTOMapperService implements Function<Customer, CustomerDTO> {

  private final CustomerIdentifierDTOMapperService customerIdentifierDTOMapperService;

  private final CustomerAddressDTOMapperService customerAddressDTOMapperService;

  public CustomerDTOMapperService(CustomerIdentifierDTOMapperService customerIdentifierDTOMapperService,
                                  CustomerAddressDTOMapperService customerAddressDTOMapperService) {
    this.customerIdentifierDTOMapperService = customerIdentifierDTOMapperService;
    this.customerAddressDTOMapperService = customerAddressDTOMapperService;
  }
  @Override
  public CustomerDTO apply(Customer customer) {

    return new CustomerDTO(customer.getId().toString(), customer.getFirstName(), customer.getLastName(),
                           customer.getCustomerIdentifiers().stream().map(customerIdentifierDTOMapperService).collect(Collectors.toList()),
                           customer.getCustomerAddresses().stream().map(customerAddressDTOMapperService).collect(Collectors.toList()));
  }
}
