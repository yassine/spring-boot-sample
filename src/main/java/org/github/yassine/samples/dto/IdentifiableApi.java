package org.github.yassine.samples.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public abstract class IdentifiableApi {
  private UUID id;
}
