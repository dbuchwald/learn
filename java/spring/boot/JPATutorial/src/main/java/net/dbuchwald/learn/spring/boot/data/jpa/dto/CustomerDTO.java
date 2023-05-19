package net.dbuchwald.learn.spring.boot.data.jpa.dto;

public class CustomerDTO {

  private final String id;

  private final String firstName;

  private final String lastName;

  public CustomerDTO(String id, String firstName, String lastName) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
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

  @Override
  public String toString() {
    return "CustomerDTO{" +
        "id='" + id + '\'' +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        '}';
  }
}
