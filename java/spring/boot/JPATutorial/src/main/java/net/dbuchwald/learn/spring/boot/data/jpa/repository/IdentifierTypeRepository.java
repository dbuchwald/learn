package net.dbuchwald.learn.spring.boot.data.jpa.repository;

import net.dbuchwald.learn.spring.boot.data.jpa.entity.IdentifierType;
import net.dbuchwald.learn.spring.boot.data.jpa.entity.IdentifierTypeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentifierTypeRepository extends JpaRepository<IdentifierType, IdentifierTypeId> {
}
