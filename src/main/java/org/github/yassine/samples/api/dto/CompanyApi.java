package org.github.yassine.samples.api.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class CompanyApi extends IdentifiableApi {
  @NotNull
  private String name;
  @Valid
  private AddressApi address;
  @Email
  private String email;
  private String phone;
}
