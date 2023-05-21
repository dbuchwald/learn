package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Embeddable
public class CustomerIdentifierId {

  @Column(name = "customer_id")
  private UUID customerId;

  @Column(name = "identifier_id")
  private Long identifierId;

  public CustomerIdentifierId() {
  }

  public CustomerIdentifierId(UUID customerId, Long identifierId) {
    this.customerId = customerId;
    this.identifierId = identifierId;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public Long getIdentifierId() {
    return identifierId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CustomerIdentifierId that = (CustomerIdentifierId) o;
    return Objects.equals(customerId, that.customerId) && Objects.equals(identifierId, that.identifierId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerId, identifierId);
  }
}
