package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Embeddable
public class CustomerIdentifierId {

  @ManyToOne
  @JoinColumn(name = "customerId", referencedColumnName = "id")
  private Customer customer;

  @ManyToOne
  @JoinColumns({
      @JoinColumn(name = "identifierCountry", referencedColumnName = "identifierCountry"),
      @JoinColumn(name = "identifierType", referencedColumnName = "identifierType")
  })
  private IdentifierType identifierType;

  public CustomerIdentifierId() {
  }

  public Customer getCustomer() {
    return customer;
  }

  public IdentifierType getIdentifierType() {
    return identifierType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomerIdentifierId that = (CustomerIdentifierId) o;
    return Objects.equals(customer, that.customer) && Objects.equals(identifierType, that.identifierType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customer, identifierType);
  }
}
