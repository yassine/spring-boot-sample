package org.github.yassine.samples.core.mapping;

import java.util.Map;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMap;
import ma.glasnost.orika.metadata.MapperKey;

public class DefaultMapperFactoryArtifact extends DefaultMapperFactory {

  public DefaultMapperFactoryArtifact(MapperFactoryBuilder<?, ?> builder) {
    super(builder);
  }

  Map<MapperKey, ClassMap<Object, Object>> getClassMapRegistry() {
    return classMapRegistry;
  }

  public static class DefaultMapperFactoryArtifactBuilder extends DefaultMapperFactory.Builder {
    @Override
    public DefaultMapperFactoryArtifact build() {
      return new DefaultMapperFactoryArtifact(this);
    }
  }

}
