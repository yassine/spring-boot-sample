package org.github.yassine.samples.api.model;

import graphql.annotations.annotationTypes.GraphQLField;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AddressApi {
  @NotNull @GraphQLField
  private String streetLine;
  @NotNull @GraphQLField
  private String city;
  @NotNull @GraphQLField
  private String country;
}
