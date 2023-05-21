package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_identifiers")
public class CustomerIdentifier {

  @EmbeddedId
  private CustomerIdentifierId customerIdentifierId;

  @ManyToOne
  @MapsId("customerId")
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne
  @MapsId("identifierId")
  @JoinColumn(name = "identifier_id")
  private IdentifierType identifierType;

  @Column(name = "identifier_value")
  private String identifierValue;

  protected CustomerIdentifier() {
  }

  public CustomerIdentifierId getCustomerIdentifierId() {
    return customerIdentifierId;
  }

  public Customer getCustomer() {
    return customer;
  }

  public IdentifierType getIdentifierType() {
    return identifierType;
  }

  public String getIdentifierValue() {
    return identifierValue;
  }

}
