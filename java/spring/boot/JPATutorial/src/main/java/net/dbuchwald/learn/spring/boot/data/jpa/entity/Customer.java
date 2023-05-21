package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid", updatable = false)
  private UUID id;
  private String firstName;
  private String lastName;

  @OneToMany(mappedBy = "customer")
  private List<CustomerIdentifier> customerIdentifiers;

  @SuppressWarnings("unused")
  protected Customer() {
  }

  public Customer(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.customerIdentifiers = new ArrayList<>();
  }

  public UUID getId() {
    return this.id;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public List<CustomerIdentifier> getCustomerIdentifiers() {
    return this.customerIdentifiers;
  }
}
