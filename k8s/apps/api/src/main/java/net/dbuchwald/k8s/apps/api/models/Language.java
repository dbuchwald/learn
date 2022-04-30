package net.dbuchwald.k8s.apps.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="languages", schema="ref_data")
public class Language {

  @Id
  @Column(name="lang_id", nullable = false)
  private final String langId;
  @Column(name="description", nullable = false)
  private String description;

  public Language() {
    this.langId = "";
    this.description = "";
  }

  public Language(String langId) {
    this.langId = langId;
  }

  public Language(String langId, String description) {
    this.langId = langId;
    this.description = description;
  }

  public String getLangId() {
    return langId;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Language language = (Language) o;
    return Objects.equals(getLangId(), language.getLangId()) && Objects.equals(getDescription(), language.getDescription());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getLangId(), getDescription());
  }
}
