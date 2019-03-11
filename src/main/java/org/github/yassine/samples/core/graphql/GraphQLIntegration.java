package org.github.yassine.samples.core.graphql;

import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

import graphql.Scalars;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import java.util.UUID;
import org.github.yassine.samples.api.model.CompanyApi;
import org.github.yassine.samples.api.model.PersonApi;
import org.springframework.context.annotation.Bean;

public class GraphQLIntegration {
  @Bean
  public GraphQLSchema graphQLSchema(GraphQLQueryService queryService){
    GraphQLAnnotations processor = GraphQLAnnotations.getInstance();
    GraphQLSchema.Builder schemaBuilder = GraphQLSchema.newSchema();
    processor.registerType(new UUIDTypeFunction());
    GraphQLObjectType querytype = newObject()
      .name("query")
      .field(newFieldDefinition()
        .name("person")
        .type(processor.getObjectHandler().getObject(PersonApi.class, processor.getContainer()))
        .argument(new GraphQLArgument.Builder().name("uuid").type(Scalars.GraphQLID).build())
        .dataFetcher( context -> queryService.person(UUID.fromString(context.getArgument("uuid"))))
      )
      .field(newFieldDefinition()
        .name("company")
        .type(processor.getObjectHandler().getObject(CompanyApi.class, processor.getContainer()))
        .argument(new GraphQLArgument.Builder().name("uuid").type(Scalars.GraphQLID).build())
        .dataFetcher( context -> queryService.company(UUID.fromString(context.getArgument("uuid"))))
      )
      .build();
    return schemaBuilder.query(querytype).build();
  }

}
