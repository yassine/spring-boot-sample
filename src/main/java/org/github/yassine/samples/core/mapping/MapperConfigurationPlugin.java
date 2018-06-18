package org.github.yassine.samples.core.mapping;

import java.util.function.Consumer;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;

/**
 * Created by yassine on 11/28/16.
 */
public interface MapperConfigurationPlugin extends Consumer<MapperFactory> {

  static MapperConfigurationPlugin defaultOf(Class<?> domainClass, Class<?> apiClass) {
    return (mapperFactory) -> {
      ClassMapBuilder<?, ?> builder = mapperFactory.classMap(domainClass, apiClass);
      builder.byDefault();
      builder.register();
    };
  }

  static MapperConfigurationPlugin defaultOfUsing(Class<?> domainClass,
                                                  Class<?> apiClass,
                                                  Class<?> parentDomainClass,
                                                  Class<?> parentApiClass) {
    return (mapperFactory) -> {
      ClassMapBuilder<?, ?> builder = mapperFactory.classMap(domainClass, apiClass);
      builder.use(parentDomainClass, parentApiClass);
      builder.byDefault();
      builder.register();
    };
  }
}
