package org.github.yassine.samples.domain.model.shared;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Embeddable
@Getter
@Setter
@Accessors(chain = true)
public class NameInformation {
  @NotNull
  private String firstName;
  private String middleName;
  @NotNull
  private String lastName;
}
