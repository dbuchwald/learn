package net.dbuchwald.learn.spring.boot.data.jpa.service.mapper;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.CustomerIdentifierDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.CustomerIdentifier;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class CustomerIdentifierDTOMapperService implements Function<CustomerIdentifier, CustomerIdentifierDTO> {

  private final IdentifierTypeDTOMapperService identifierTypeDTOMapperService;

  public CustomerIdentifierDTOMapperService(IdentifierTypeDTOMapperService identifierTypeDTOMapperService) {
    this.identifierTypeDTOMapperService = identifierTypeDTOMapperService;
  }
  @Override
  public CustomerIdentifierDTO apply(CustomerIdentifier customerIdentifier) {
    return new CustomerIdentifierDTO(identifierTypeDTOMapperService.apply(customerIdentifier.getIdentifierType()),
                                     customerIdentifier.getIdentifierValue());
  }
}
