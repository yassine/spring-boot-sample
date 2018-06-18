package org.github.yassine.samples.domain.model.company;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.github.yassine.samples.domain.model.identity.AuditableEntity;
import org.github.yassine.samples.domain.model.shared.NameInformation;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Person extends AuditableEntity {
  @Embedded
  @NotNull
  @Valid
  private NameInformation name = new NameInformation();
}
