package org.github.yassine.samples.domain.service.support;

import java.util.Optional;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.BoundMapperFacade;
import org.github.yassine.samples.api.model.CompanyApi;
import org.github.yassine.samples.domain.model.company.Company;
import org.github.yassine.samples.domain.repository.CompanyRepository;
import org.github.yassine.samples.domain.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
@Transactional
public class CompanyServiceSupport implements CompanyService {

  private final CompanyRepository repository;
  @Getter
  private final BoundMapperFacade<CompanyApi, Company> companyMapper;

  public CompanyApi save(CompanyApi api) {
    Company company = companyMapper.map(api);
    repository.save(company);
    return companyMapper.mapReverse(company);
  }

  public Optional<CompanyApi> update(CompanyApi api) {
    Optional<Company> company = repository.findByUuid(api.getId())
      .map(c -> companyMapper.map(api, c));
    company.ifPresent(repository::save);
    return company.map(companyMapper::mapReverse);
  }

  public Optional<CompanyApi> findByUuid(UUID uuid) {
    return repository.findByUuid(uuid)
      .map(companyMapper::mapReverse);
  }

  public Page<CompanyApi> list(int page, int maxItems) {
    return list(page, maxItems, Sort.unsorted());
  }

  public Page<CompanyApi> list(int page, int maxItems, Sort sort) {
    return repository.findAll(PageRequest.of(page, maxItems, sort))
              .map(companyMapper::mapReverse);
  }

  public void delete(UUID companyUuid) {
    repository.findByUuid(companyUuid)
      .ifPresent(repository::delete);
  }

}
