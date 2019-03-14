package org.github.yassine.samples.api.rest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.github.yassine.samples.api.model.CompanyApi;
import org.github.yassine.samples.api.model.PersonApi;
import org.github.yassine.samples.domain.service.CompanyService;
import org.github.yassine.samples.domain.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/company")
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
public class CompanyRestController {

  private final CompanyService companyService;
  private final PersonService personService;

  @PostMapping
  public CompanyApi onCreate(@Valid @RequestBody CompanyApi companyApi) {
    return companyService.save(companyApi);
  }

  @PutMapping
  public Optional<CompanyApi> onUpdate(@Valid @RequestBody CompanyApi companyApi) {
    return companyService.update(companyApi);
  }

  @GetMapping
  public Page<CompanyApi> list(@RequestParam(value = "page", defaultValue = "0") Integer page,
                        @RequestParam(value = "size", defaultValue = "10") Integer pageSize) {
    return companyService.list(page, pageSize);
  }

  @DeleteMapping("/{companyId}")
  public void onDelete(@PathVariable UUID companyId) {
    companyService.delete(companyId);
  }

  @GetMapping(value = "/{companyId}")
  public Optional<CompanyApi> get(@PathVariable UUID companyId) {
    return companyService.findByUuid(companyId);
  }

  @PostMapping(value = "/{companyId}/owner")
  public PersonApi onOwnerAdd(@Valid @RequestBody PersonApi personApi, @PathVariable UUID companyId) {
    return personService.addCompanyOwner(companyId, personApi);
  }

  @GetMapping(value = "/{companyId}/owner")
  public Optional<List<PersonApi>> owners(@PathVariable UUID companyId) {
    return personService.getOwners(companyId);
  }
}
