package org.github.yassine.samples.domain.service.support;

import static java.lang.String.format;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.BoundMapperFacade;
import org.github.yassine.samples.api.model.PersonApi;
import org.github.yassine.samples.domain.model.company.Company;
import org.github.yassine.samples.domain.model.company.Person;
import org.github.yassine.samples.domain.repository.CompanyRepository;
import org.github.yassine.samples.domain.repository.PersonRepository;
import org.github.yassine.samples.domain.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
public class PersonServiceSupport implements PersonService {

  private final CompanyRepository companyRepository;
  private final PersonRepository personRepository;
  private final BoundMapperFacade<PersonApi, Person> mapper;

  public PersonApi addCompanyOwner(UUID companyId, PersonApi personApi) {
    Person person = mapper.map(personApi);
    personRepository.save(person);
    return addCompanyOwner(companyId, person);
  }

  public PersonApi addCompanyOwner(UUID companyId, Person person) {
    Company company = companyRepository.findByUuid(companyId)
      .orElseThrow(() -> new RuntimeException(
        format("Unable to find company with id : '%s'", companyId)));
    company.getOwners().add(person);
    companyRepository.save(company);
    return mapper.mapReverse(person);
  }

  @Override
  public Optional<PersonApi> findByUUID(UUID uuid) {
    return personRepository.findByUuid(uuid).map(mapper::mapReverse);
  }

  @Transactional
  public Optional<List<PersonApi>> getOwners(UUID companyId) {
    return companyRepository.findByUuid(companyId).map(Company::getOwners)
      .map(owners -> owners.stream().map(mapper::mapReverse)
        .collect(Collectors.toList()));
  }

}
