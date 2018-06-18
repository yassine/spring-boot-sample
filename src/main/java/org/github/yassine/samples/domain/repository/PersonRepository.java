package org.github.yassine.samples.domain.repository;

import java.util.UUID;
import org.github.yassine.samples.domain.model.company.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {
  Person findByUuid(UUID uuid);
}
