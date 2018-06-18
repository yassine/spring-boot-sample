package org.github.yassine.samples.domain.repository;

import java.util.Optional;
import java.util.UUID;
import org.github.yassine.samples.domain.model.identity.AuditableEntity;

public interface DomainRepository<E extends AuditableEntity> {
  Optional<E> findByUuid(UUID uuid);
}
