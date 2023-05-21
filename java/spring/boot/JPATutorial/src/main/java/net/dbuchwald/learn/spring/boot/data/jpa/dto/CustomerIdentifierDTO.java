package net.dbuchwald.learn.spring.boot.data.jpa.dto;

public class CustomerIdentifierDTO {

  private final IdentifierTypeDTO identifierType;

  private final String value;

  public CustomerIdentifierDTO(IdentifierTypeDTO identifierType, String value) {
    this.identifierType = identifierType;
    this.value = value;
  }

  public IdentifierTypeDTO getIdentifierType() {
    return identifierType;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return "CustomerIdentifierDTO{" +
        "identifierType=" + identifierType +
        ", value='" + value + '\'' +
        '}';
  }
}
