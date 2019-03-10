package org.github.yassine.samples.api.model;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
@GraphQLName("company")
public class CompanyApi extends IdentifiableApi {
  @NotNull @GraphQLField
  private String name;
  @Valid @GraphQLField
  private AddressApi address;
  @Email @GraphQLField
  private String email;
  @GraphQLField
  private String phone;
}
