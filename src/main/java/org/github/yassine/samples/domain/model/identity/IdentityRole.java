package org.github.yassine.samples.domain.model.identity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
public class IdentityRole extends AuditableEntity {
  private String role;
  @ManyToOne
  private Identity identity;
}
