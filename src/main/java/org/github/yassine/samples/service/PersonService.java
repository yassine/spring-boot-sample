package org.github.yassine.samples.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.BoundMapperFacade;
import org.github.yassine.samples.domain.model.company.Company;
import org.github.yassine.samples.domain.model.company.Person;
import org.github.yassine.samples.domain.repository.CompanyRepository;
import org.github.yassine.samples.domain.repository.PersonRepository;
import org.github.yassine.samples.dto.PersonApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
public class PersonService {

  private final CompanyRepository companyRepository;
  private final PersonRepository personRepository;
  private final BoundMapperFacade<PersonApi, Person> personBoundMapperFacade;

  public PersonApi addCompanyOwner(UUID companyId, PersonApi personApi) {
    Person person = personBoundMapperFacade.map(personApi);
    personRepository.save(person);
    return addCompanyOwner(companyId, person);
  }

  public PersonApi addCompanyOwner(UUID companyId, Person person) {
    Company company = companyRepository.findByUuid(companyId);
    company.getOwners().add(person);
    companyRepository.save(company);
    return personBoundMapperFacade.mapReverse(person);
  }

  @Transactional
  public List<PersonApi> getOwners(UUID companyId) {
    return companyRepository.findByUuid(companyId).getOwners().stream()
      .map(personBoundMapperFacade::mapReverse)
      .collect(Collectors.toList());
  }

}
