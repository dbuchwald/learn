package net.dbuchwald.learn.spring.boot.data.jpa.repository;

import net.dbuchwald.learn.spring.boot.data.jpa.entity.IdentifierType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentifierTypeRepository extends JpaRepository<IdentifierType, Long> {
  Optional<IdentifierType> findByCountryAndIdType(String country, String type);
}
