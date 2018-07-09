package org.github.yassine.samples.domain.repository;

import org.github.yassine.samples.domain.model.identity.Identity;
import org.github.yassine.samples.domain.model.identity.IdentityProvider;
import org.springframework.data.repository.CrudRepository;

public interface IdentityRepository extends DomainRepository<Identity>, CrudRepository<Identity, Long> {
  Identity findByExternalIdAndProvider(String externalId, IdentityProvider identityProvider);
}
