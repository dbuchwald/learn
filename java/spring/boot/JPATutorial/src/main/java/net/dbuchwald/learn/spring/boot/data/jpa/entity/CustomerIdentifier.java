package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_identifier")
public class CustomerIdentifier {

  @EmbeddedId
  private CustomerIdentifierId customerIdentifierId;

  private String identifierValue;

  protected CustomerIdentifier() {
  }

  public Customer getCustomer() {
    return customerIdentifierId.getCustomer();
  }

  public IdentifierType getIdentifierType() {
    return customerIdentifierId.getIdentifierType();
  }

  public String getIdentifierValue() {
    return identifierValue;
  }

}
