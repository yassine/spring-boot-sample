package org.github.yassine.samples.rest;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.github.yassine.samples.domain.repository.CompanyRepository;
import org.github.yassine.samples.dto.CompanyApi;
import org.github.yassine.samples.dto.PersonApi;
import org.github.yassine.samples.service.CompanyService;
import org.github.yassine.samples.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/company")
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
public class CompanyRestController {

  private final CompanyService companyService;
  private final CompanyRepository companyRepository;
  private final PersonService personService;

  @RequestMapping(value = "/", method = POST)
  CompanyApi onCreate(@Valid @RequestBody CompanyApi companyApi) {
    return companyService.save(companyApi);
  }

  @RequestMapping(value = "/{companyId}", method = GET)
  CompanyApi get(@PathVariable UUID companyId) {
    return companyService.findByUuid(companyId);
  }

  @RequestMapping(value = "/{companyId}/owner", method = POST)
  @Transactional
  PersonApi onOwnerAdd(@Valid @RequestBody PersonApi personApi, @PathVariable UUID companyId) {
    return personService.addCompanyOwner(companyId, personApi);
  }

  @RequestMapping(value = "/{companyId}/owner", method = GET)
  @Transactional
  List<PersonApi> owners(@PathVariable UUID companyId) {
    return personService.getOwners(companyId);
  }
}
