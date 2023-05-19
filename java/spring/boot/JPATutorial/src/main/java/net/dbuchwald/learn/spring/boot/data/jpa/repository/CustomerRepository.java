package net.dbuchwald.learn.spring.boot.data.jpa.repository;

import net.dbuchwald.learn.spring.boot.data.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

  List<Customer> findByLastName(String lastName);
}
