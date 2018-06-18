package org.github.yassine.samples.service;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.BoundMapperFacade;
import org.github.yassine.samples.domain.model.company.Company;
import org.github.yassine.samples.domain.repository.CompanyRepository;
import org.github.yassine.samples.dto.CompanyApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
@Transactional
public class CompanyService {

  private final CompanyRepository repository;
  private final BoundMapperFacade<CompanyApi, Company> companyMapper;

  public CompanyApi save(CompanyApi api) {
    Company company = companyMapper.map(api);
    repository.save(company);
    return companyMapper.mapReverse(company);
  }

  public CompanyApi findByUuid(UUID uuid) {
    return companyMapper.mapReverse(repository.findByUuid(uuid));
  }
}
