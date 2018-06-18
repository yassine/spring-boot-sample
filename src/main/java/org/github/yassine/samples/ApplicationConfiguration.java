package org.github.yassine.samples;

import org.github.yassine.samples.core.PersistenceIntegration;
import org.github.yassine.samples.core.mapping.MappingBeanPostProducer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import( {PersistenceIntegration.class, MappingBeanPostProducer.class})
@Configuration
public class ApplicationConfiguration {
}
