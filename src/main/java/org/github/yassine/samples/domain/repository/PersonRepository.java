package org.github.yassine.samples.domain.repository;

import org.github.yassine.samples.domain.model.company.Person;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends PagingAndSortingRepository<Person, Long>, DomainRepository<Person> {
}
