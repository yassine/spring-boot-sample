package org.github.yassine.samples.api.model;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class PersonApi extends IdentifiableApi {
  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  private String middleName;
}
