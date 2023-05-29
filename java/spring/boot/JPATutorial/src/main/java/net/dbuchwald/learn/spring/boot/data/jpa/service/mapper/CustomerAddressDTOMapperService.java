package net.dbuchwald.learn.spring.boot.data.jpa.service.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerAddressDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.CustomerAddress;

@Service
public class CustomerAddressDTOMapperService implements Function<CustomerAddress, CustomerAddressDTO> {

  @Override
  public CustomerAddressDTO apply(CustomerAddress customerAddress) {
    return new CustomerAddressDTO(customerAddress.getAddressId().toString(), 
                                  customerAddress.getAddressType(), 
                                  customerAddress.getStreetAddress(), 
                                  customerAddress.getZipCode(), 
                                  customerAddress.getCity(), 
                                  customerAddress.getCountry());
  }
  
}
