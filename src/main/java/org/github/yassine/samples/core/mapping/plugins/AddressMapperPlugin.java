package org.github.yassine.samples.core.mapping.plugins;

import ma.glasnost.orika.MapperFactory;
import org.github.yassine.samples.core.mapping.MapperConfigurationPlugin;
import org.github.yassine.samples.domain.model.shared.AddressInformation;
import org.github.yassine.samples.api.dto.AddressApi;

public class AddressMapperPlugin implements MapperConfigurationPlugin {
  @Override
  public void accept(MapperFactory mapperFactory) {
    mapperFactory.classMap(AddressApi.class, AddressInformation.class)
      .byDefault()
      .register();
  }
}
