package org.github.yassine.samples;

import org.github.yassine.samples.core.persistence.PersistenceIntegration;
import org.github.yassine.samples.core.mapping.MappingConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import( {PersistenceIntegration.class, MappingConfiguration.class})
@Configuration
public class ApplicationConfiguration {
}
