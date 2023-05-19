package net.dbuchwald.learn.spring.boot.data.jpa.service.mapper;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerDTOMapperService implements Function<Customer, CustomerDTO> {
  @Override
  public CustomerDTO apply(Customer customer) {
    return new CustomerDTO(customer.getId().toString(), customer.getFirstName(), customer.getLastName());
  }
}
