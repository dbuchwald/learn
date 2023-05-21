package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "identifier_type")
public class IdentifierType {

  @EmbeddedId
  private IdentifierTypeId identifierTypeId;

  private String identifierDescription;

  protected IdentifierType() {
  }

  public IdentifierType(IdentifierTypeId identifierTypeId, String identifierDescription) {
    this.identifierTypeId = identifierTypeId;
    this.identifierDescription = identifierDescription;
  }

  public IdentifierTypeId getIdentifierTypeId() {
    return identifierTypeId;
  }

  public String getIdentifierDescription() {
    return identifierDescription;
  }
}
