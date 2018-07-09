package org.github.yassine.samples.domain.model.identity;

import javax.persistence.Entity;
import lombok.Getter;

@Entity @Getter
public class IdentityProvider extends AuditableEntity {
  private String name;
  private String description;
}
