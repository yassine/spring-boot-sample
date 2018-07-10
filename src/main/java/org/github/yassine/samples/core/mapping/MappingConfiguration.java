package org.github.yassine.samples.core.mapping;

import static org.apache.commons.lang3.reflect.TypeUtils.parameterize;
import static org.github.yassine.samples.core.utils.BindingUtils.bindParametrizedType;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.BoundMapperFacade;
import org.github.yassine.samples.Application;
import org.github.yassine.samples.api.model.IdentifiableApi;
import org.github.yassine.samples.domain.model.shared.UUIDIdentifiable;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

@SuppressWarnings("NullableProblems")
@Slf4j
public class MappingConfiguration implements BeanDefinitionRegistryPostProcessor {

  private static final String BOUND_MAPPER_SUFFIX = "BoundMapper";
  private final Orika orika = new Orika.OrikaBuilder()
    .autoDiscover(Application.class.getPackage().getName())
    .register(mpf ->
      mpf.classMap(UUIDIdentifiable.class, IdentifiableApi.class)
        .fieldAToB("uuid", "id")
        .register()
    )
    .build();
  private final DefaultMapperFactoryArtifact mapperFactory = new DefaultMapperFactoryArtifact.DefaultMapperFactoryArtifactBuilder().build();

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
    orika.getPlugins().forEach(plugin -> plugin.accept(mapperFactory));
    orika.getConverters().forEach(plugin -> mapperFactory.getConverterFactory().registerConverter(plugin));
    mapperFactory.getClassMapRegistry().keySet().forEach(key -> {
      final BoundMapperFacade<?, ?> boundMapperFacade = mapperFactory.getMapperFacade(key.getAType().getRawType(), key.getBType().getRawType());
      final BoundMapperFacade<?, ?> reverseBoundMapperFacade = mapperFactory.getMapperFacade(key.getBType().getRawType(), key.getAType().getRawType());
      final String directName = Joiner.on("").join(getClass().getPackage().getName(), ".",
        key.getAType().getSimpleName(), key.getBType().getSimpleName(), BOUND_MAPPER_SUFFIX);
      final String reverseName = Joiner.on("").join(getClass().getPackage().getName(), ".",
        key.getBType().getSimpleName(), key.getAType().getSimpleName(), BOUND_MAPPER_SUFFIX);
      registry.registerBeanDefinition(directName, bindParametrizedType(BoundMapperFacade.class, parameterize(BoundMapperFacade.class, key.getAType().getRawType(), key.getBType().getRawType()), () -> boundMapperFacade ));
      registry.registerBeanDefinition(reverseName, bindParametrizedType(BoundMapperFacade.class, parameterize(BoundMapperFacade.class, key.getBType().getRawType(), key.getAType().getRawType()), () -> reverseBoundMapperFacade ));
    });
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    orika.getConverters().forEach(beanFactory::autowireBean);
    orika.getPlugins().forEach(beanFactory::autowireBean);
  }

}