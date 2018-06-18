package org.github.yassine.samples.core.mapping.plugins;

import java.util.UUID;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.metadata.Type;

public class StringUUIDConverter extends CustomConverter<String, UUID> {
  @Override
  public UUID convert(String source, Type<? extends UUID> destinationType) {
    return UUID.fromString(source);
  }
}
