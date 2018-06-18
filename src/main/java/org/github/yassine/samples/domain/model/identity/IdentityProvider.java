package org.github.yassine.samples.domain.model.identity;

import javax.persistence.Entity;

@Entity
public class IdentityProvider extends AuditableEntity {
  private String name;
  private String description;
}
