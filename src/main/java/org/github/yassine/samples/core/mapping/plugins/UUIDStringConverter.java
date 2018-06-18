package org.github.yassine.samples.core.mapping.plugins;

import java.util.UUID;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

public class UUIDStringConverter extends CustomConverter<UUID, String> {
  @Override
  public String convert(UUID source, Type<? extends String> destinationType) {
    return source.toString();
  }
}
