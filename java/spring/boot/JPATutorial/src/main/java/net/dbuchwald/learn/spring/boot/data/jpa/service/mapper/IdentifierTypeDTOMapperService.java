package net.dbuchwald.learn.spring.boot.data.jpa.service.mapper;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.IdentifierTypeDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.IdentifierType;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IdentifierTypeDTOMapperService implements Function<IdentifierType, IdentifierTypeDTO> {
  @Override
  public IdentifierTypeDTO apply(IdentifierType identifierType) {
    return new IdentifierTypeDTO(identifierType.getIdentifierTypeId().getIdentifierCountry(),
                                 identifierType.getIdentifierTypeId().getIdentifierType(),
                                 identifierType.getIdentifierDescription());
  }
}
