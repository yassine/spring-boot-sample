package org.github.yassine.samples.core.graphql;

import graphql.Scalars;
import graphql.annotations.processor.ProcessingElementsContainer;
import graphql.annotations.processor.typeFunctions.TypeFunction;
import graphql.schema.GraphQLType;
import java.lang.reflect.AnnotatedType;
import java.util.UUID;

public class UUIDTypeFunction implements TypeFunction {
  @Override
  public boolean canBuildType(Class<?> aClass, AnnotatedType annotatedType) {
    return aClass.equals(UUID.class);
  }

  @Override
  public GraphQLType buildType(boolean input, Class<?> aClass, AnnotatedType annotatedType, ProcessingElementsContainer container) {
    return Scalars.GraphQLID;
  }
}
