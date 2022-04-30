package net.dbuchwald.k8s.apps.api.models;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="greetings", schema="ref_data")
public class Greeting {

  @EmbeddedId
  private GreetingId greetingId;

  @Column(name = "text", nullable = false)
  private String text;

  public Greeting() {
  }

  public Greeting(GreetingId greetingId, String text) {
    this.greetingId = greetingId;
    this.text = text;
  }

  public GreetingId getGreetingId() {
    return greetingId;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }
}
