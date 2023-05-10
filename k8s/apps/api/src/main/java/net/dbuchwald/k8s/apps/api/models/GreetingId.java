package net.dbuchwald.k8s.apps.api.models;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public record GreetingId(@Column(name = "lang_id", nullable = false) String langId,
                         @Column(name = "code", nullable = false) String code) implements Serializable {

  public static GreetingId of(String langId, String code) {
    return new GreetingId(langId, code);
  }

}
