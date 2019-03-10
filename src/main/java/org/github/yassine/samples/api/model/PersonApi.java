package org.github.yassine.samples.api.model;

import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain = true)
@GraphQLName("person")
public class PersonApi extends IdentifiableApi {
  @NotNull @GraphQLField
  private String firstName;
  @NotNull @GraphQLField
  private String lastName;
  @GraphQLField
  private String middleName;
}
