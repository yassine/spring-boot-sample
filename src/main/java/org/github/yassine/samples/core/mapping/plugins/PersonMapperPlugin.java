package org.github.yassine.samples.core.mapping.plugins;

import ma.glasnost.orika.MapperFactory;
import org.github.yassine.samples.core.mapping.MapperConfigurationPlugin;
import org.github.yassine.samples.domain.model.company.Person;
import org.github.yassine.samples.domain.model.shared.UUIDIdentifiable;
import org.github.yassine.samples.api.dto.IdentifiableApi;
import org.github.yassine.samples.api.dto.PersonApi;

public class PersonMapperPlugin implements MapperConfigurationPlugin {
  @Override
  public void accept(MapperFactory mapperFactory) {
    mapperFactory.classMap(Person.class, PersonApi.class)
      .use(UUIDIdentifiable.class, IdentifiableApi.class)
      .field("name.firstName", "firstName")
      .field("name.lastName", "lastName")
      .field("name.middleName", "middleName")
      .register();
  }
}
