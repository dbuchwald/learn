package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "identifier_types")
public class IdentifierType {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Long id;

  @Column(name = "id_country")
  private String country;

  @Column(name = "id_type")
  private String idType;

  @Column(name = "description")
  private String description;

  protected IdentifierType() {
  }

  public IdentifierType(String country, String idType, String description) {
    this.country = country;
    this.idType = idType;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public String getCountry() {
    return country;
  }

  public String getIdType() {
    return idType;
  }

  public String getDescription() {
    return description;
  }
}
