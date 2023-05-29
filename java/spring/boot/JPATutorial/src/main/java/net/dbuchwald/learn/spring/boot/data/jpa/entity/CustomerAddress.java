package net.dbuchwald.learn.spring.boot.data.jpa.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_addresses")
public class CustomerAddress {
  @Id
  @GeneratedValue
  @Column(columnDefinition = "uuid", updatable = false)
  private UUID addressId;

  @Column(columnDefinition = "uuid", updatable = false)
  private UUID customerId;

  private char addressType;

  private String streetAddress;

  private String zipCode;

  private String city;

  private String country;

  @ManyToOne
  @MapsId("customerId")
  @JoinColumn(name = "customerId")
  private Customer customer;

  protected CustomerAddress() {}

  public CustomerAddress(UUID customerId, char addressType, String streetAddress, String zipCode, String city,
      String country) {
    this.customerId = customerId;
    this.addressType = addressType;
    this.streetAddress = streetAddress;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
  }

  public UUID getAddressId() {
    return addressId;
  }

  public UUID getCustomerId() {
    return customerId;
  }

  public char getAddressType() {
    return addressType;
  }

  public String getStreetAddress() {
    return streetAddress;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  
}
