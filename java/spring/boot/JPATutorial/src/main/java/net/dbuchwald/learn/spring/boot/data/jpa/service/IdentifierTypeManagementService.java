package net.dbuchwald.learn.spring.boot.data.jpa.service;

import net.dbuchwald.learn.spring.boot.data.jpa.dto.IdentifierTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IdentifierTypeManagementService {

  Optional<IdentifierTypeDTO> findIdentifierType(String country, String type);

  List<IdentifierTypeDTO> findAll();
}
