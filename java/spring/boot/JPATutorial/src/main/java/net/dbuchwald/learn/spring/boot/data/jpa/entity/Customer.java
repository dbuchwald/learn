package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

import java.util.UUID;


@Entity
public class Customer {
  @Id
  @GeneratedValue
  @Column( columnDefinition = "uuid", updatable = false )
  private UUID id;
  private String firstName;
  private String lastName;

  @SuppressWarnings("unused")

  protected Customer() {
  }

  public Customer(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
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
}
