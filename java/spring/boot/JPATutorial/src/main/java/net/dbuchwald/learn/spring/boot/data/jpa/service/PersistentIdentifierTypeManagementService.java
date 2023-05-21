package net.dbuchwald.learn.spring.boot.data.jpa.service;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.IdentifierTypeDTO;
import net.dbuchwald.learn.spring.boot.data.jpa.repository.IdentifierTypeRepository;
import net.dbuchwald.learn.spring.boot.data.jpa.service.mapper.IdentifierTypeDTOMapperService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PersistentIdentifierTypeManagementService implements IdentifierTypeManagementService {

  private final IdentifierTypeRepository identifierTypeRepository;
  private final IdentifierTypeDTOMapperService identifierTypeDTOMapperService;

  public PersistentIdentifierTypeManagementService(IdentifierTypeRepository identifierTypeRepository,
                                                   IdentifierTypeDTOMapperService identifierTypeDTOMapperService) {
    this.identifierTypeRepository = identifierTypeRepository;
    this.identifierTypeDTOMapperService = identifierTypeDTOMapperService;
  }
  @Override
  public Optional<IdentifierTypeDTO> findIdentifierType(String country, String type) {
    return identifierTypeRepository.findByCountryAndIdType(country, type).map(identifierTypeDTOMapperService);
  }

  @Override
  public List<IdentifierTypeDTO> findAll() {
    return identifierTypeRepository.findAll().stream().map(identifierTypeDTOMapperService).collect(Collectors.toList());
  }
}
