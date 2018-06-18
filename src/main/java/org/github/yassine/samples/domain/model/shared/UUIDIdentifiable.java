package org.github.yassine.samples.domain.model.shared;

import java.util.UUID;
import javax.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
public abstract class UUIDIdentifiable {
  private UUID uuid = UUID.randomUUID();
}
