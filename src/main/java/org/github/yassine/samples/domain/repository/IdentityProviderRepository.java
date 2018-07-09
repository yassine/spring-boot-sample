package org.github.yassine.samples.domain.repository;

import org.github.yassine.samples.domain.model.identity.IdentityProvider;
import org.springframework.data.repository.CrudRepository;

public interface IdentityProviderRepository extends DomainRepository<IdentityProvider>, CrudRepository<IdentityProvider, Long> {
  IdentityProvider findByName(String name);
}
