package net.dbuchwald.learn.spring.boot.data.jpa.dto;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class CustomerDTO {

  private final String id;

  private final String firstName;

  private final String lastName;

  private final List<CustomerIdentifierDTO> customerIdentifiers;

  private final List<CustomerAddressDTO> customerAddresses;

  public CustomerDTO(String id, String firstName, String lastName, List<CustomerIdentifierDTO> customerIdentifiers, List<CustomerAddressDTO> customerAddresses) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.customerIdentifiers = customerIdentifiers;
    this.customerAddresses = customerAddresses;
  }

  @SuppressWarnings("unused")
  public String getId() {
    return id;
  }

  @SuppressWarnings("unused")
  public String getFirstName() {
    return firstName;
  }

  @SuppressWarnings("unused")
  public String getLastName() {
    return lastName;
  }

  @SuppressWarnings("unused")
  public List<CustomerIdentifierDTO> getCustomerIdentifiers() {
    return customerIdentifiers;
  }

  @SuppressWarnings("unused")
  public List<CustomerAddressDTO> getCustomerAddresses() {
    return customerAddresses;
  }

  @Override
  public String toString() {
    return "CustomerDTO{" +
        "id='" + id + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", identifiers=[" + customerIdentifiers.stream().map(CustomerIdentifierDTO::toString).collect(joining(",")) + "]," +
        ", addresses=[" + customerAddresses.stream().map(CustomerAddressDTO::toString).collect(joining(",")) + ']' +
        '}';
  }
}
