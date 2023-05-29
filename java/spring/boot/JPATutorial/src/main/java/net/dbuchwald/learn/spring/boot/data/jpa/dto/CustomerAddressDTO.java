package net.dbuchwald.learn.spring.boot.data.jpa.dto;

public class CustomerAddressDTO {
  
  private String addressId;

  private char addressType;

  private String streetAddress;

  private String zipCode;

  private String city;

  private String country;

  public CustomerAddressDTO(String addressId, char addressType, String streetAddress, String zipCode,
      String city, String country) {
    this.addressId = addressId;
    this.addressType = addressType;
    this.streetAddress = streetAddress;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
  }

  public String getAddressId() {
    return addressId;
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

  @Override
  public String toString() {
    return "CustomerAddressDTO{addressId='" + addressId + "', addressType='" + addressType
        + "', streetAddress='" + streetAddress + "', zipCode='" + zipCode + "', city='" + city 
        + "', country='" + country + "'}";
  }
}
