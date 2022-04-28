package net.dbuchwald.k8s.apps.api.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GreetingId implements Serializable {

  @Column(name = "lang_id", nullable = false)
  private String langId;

  @Column(name = "code", nullable = false)
  private String code;

  public GreetingId() {
  }

  public GreetingId(String langId, String code) {
    this.langId = langId;
    this.code = code;
  }

  public String getLangId() {
    return langId;
  }

  public String getCode() {
    return code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GreetingId that = (GreetingId) o;
    return Objects.equals(langId, that.langId) && Objects.equals(code, that.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(langId, code);
  }
}
