package net.dbuchwald.learn.spring.boot.data.jpa.dto;

public class CustomerIdentifierDTO {

  private final String id;

  private final IdentifierTypeDTO identifierType;

  private final String value;

  public CustomerIdentifierDTO(String id, IdentifierTypeDTO identifierType, String value) {
    this.id = id;
    this.identifierType = identifierType;
    this.value = value;
  }

  public String getId() {
    return id;
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
        "id='" + id + '\'' +
        ", identifierType=" + identifierType +
        ", value='" + value + '\'' +
        '}';
  }
}
