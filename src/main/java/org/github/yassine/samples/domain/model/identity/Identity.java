package org.github.yassine.samples.domain.model.identity;

import static javax.persistence.CascadeType.ALL;
import static org.hibernate.annotations.CacheConcurrencyStrategy.NONSTRICT_READ_WRITE;

import java.util.Map;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapKey;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;

@Entity @Getter @Setter
public class Identity extends AuditableEntity {

  private String externalId;

  @ManyToOne
  private IdentityProvider provider;

  @OneToMany(cascade = ALL, mappedBy = "identity", orphanRemoval = true)
  @MapKey(name = "role")
  @Cache(usage = NONSTRICT_READ_WRITE)
  private Map<String, IdentityRole> roles;
}
