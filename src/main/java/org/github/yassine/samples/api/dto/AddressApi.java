package org.github.yassine.samples.api.dto;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AddressApi {
  @NotNull
  private String streetLine;
  @NotNull
  private String city;
  @NotNull
  private String country;
}
