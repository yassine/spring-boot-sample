package org.github.yassine.samples.core.mapping.plugins;

import ma.glasnost.orika.MapperFactory;
import org.github.yassine.samples.api.model.CompanyApi;
import org.github.yassine.samples.api.model.IdentifiableApi;
import org.github.yassine.samples.core.mapping.MapperConfigurationPlugin;
import org.github.yassine.samples.domain.model.company.Company;
import org.github.yassine.samples.domain.model.shared.UUIDIdentifiable;

public class CompanyMapperPlugin implements MapperConfigurationPlugin {
  @Override
  public void accept(MapperFactory mapperFactory) {
    mapperFactory.classMap(Company.class, CompanyApi.class)
      .use(UUIDIdentifiable.class, IdentifiableApi.class)
      .exclude("id")
      .byDefault()
      .register();
  }
}
