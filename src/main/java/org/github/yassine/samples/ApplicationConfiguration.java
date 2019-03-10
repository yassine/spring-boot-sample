package org.github.yassine.samples;

import org.github.yassine.samples.core.graphql.GraphQLIntegration;
import org.github.yassine.samples.core.mapping.MappingConfiguration;
import org.github.yassine.samples.core.persistence.PersistenceIntegration;
import org.github.yassine.samples.core.security.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import( {PersistenceIntegration.class, MappingConfiguration.class, SecurityConfiguration.class, GraphQLIntegration.class})
@Configuration
public class ApplicationConfiguration {
}
