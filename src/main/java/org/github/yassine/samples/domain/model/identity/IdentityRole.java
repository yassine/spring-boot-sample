package org.github.yassine.samples.domain.model.identity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import lombok.Getter;
import org.github.yassine.samples.core.security.authorization.Role;

@Entity @Getter
public class IdentityRole extends AuditableEntity {

  @Enumerated(EnumType.STRING)
  private Role role;

  @ManyToOne
  private Identity identity;
}
