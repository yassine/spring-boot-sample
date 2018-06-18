package org.github.yassine.samples.domain.model.shared;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Embeddable
public class AddressInformation {
  @NotNull
  private String streetLine;
  @NotNull
  private String city;
  @NotNull
  private String country;
}
