package org.github.yassine.samples.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.github.yassine.samples.api.model.PersonApi;
import org.github.yassine.samples.domain.model.company.Person;

public interface PersonService {
  PersonApi addCompanyOwner(UUID companyId, PersonApi personApi);
  PersonApi addCompanyOwner(UUID companyId, Person person);
  Optional<PersonApi> findByUUID(UUID uuid);
  Optional<List<PersonApi>> getOwners(UUID companyId);
}
