package net.dbuchwald.learn.spring.boot.data.jpa.repository;

import net.dbuchwald.learn.spring.boot.data.jpa.entity.CustomerIdentifier;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.CustomerIdentifierId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerIdentifierRepository extends JpaRepository<CustomerIdentifier, CustomerIdentifierId> {

  Optional<CustomerIdentifier> findByIdentifierTypeIdAndIdentifierValue(Long id, String value);
}
