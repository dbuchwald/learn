package net.dbuchwald.learn.spring.boot.data.jpa.dto;

public class IdentifierTypeDTO {

  private final String country;

  private final String type;

  private final String description;

  public IdentifierTypeDTO(String country, String type, String description) {
    this.country = country;
    this.type = type;
    this.description = description;
  }

  public String getCountry() {
    return country;
  }

  public String getType() {
    return type;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public String toString() {
    return "IdentifierTypeDTO{" +
        "country='" + country + '\'' +
        ", type='" + type + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}
