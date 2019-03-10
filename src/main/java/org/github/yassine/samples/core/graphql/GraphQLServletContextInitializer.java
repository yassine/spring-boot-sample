package org.github.yassine.samples.core.graphql;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLHttpServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.stereotype.Component;

@Component
public class GraphQLServletContextInitializer implements ServletContextInitializer {
  @Autowired
  private GraphQLSchema schema;

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    ServletRegistration.Dynamic registration = servletContext.addServlet("graphql", SimpleGraphQLHttpServlet.newBuilder(schema).build());
    registration.addMapping("/graphql");
    registration.setLoadOnStartup(1);
  }
}
