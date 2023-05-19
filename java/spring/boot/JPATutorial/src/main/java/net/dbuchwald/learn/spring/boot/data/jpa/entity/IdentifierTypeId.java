package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class IdentifierTypeId implements Serializable {

  private String identifierCountry;
  private String identifierType;

  protected IdentifierTypeId() {
  }

  public IdentifierTypeId(String identifierCountry, String identifierType) {
    this.identifierCountry = identifierCountry;
    this.identifierType = identifierType;
  }

  public String getIdentifierCountry() {
    return identifierCountry;
  }

  public String getIdentifierType() {
    return identifierType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    IdentifierTypeId that = (IdentifierTypeId) o;
    return Objects.equals(identifierCountry, that.identifierCountry) && Objects.equals(identifierType, that.identifierType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(identifierCountry, identifierType);
  }
}
