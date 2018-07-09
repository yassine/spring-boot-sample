package org.github.yassine.samples.domain.service;

import java.util.Optional;
import java.util.UUID;
import org.github.yassine.samples.api.model.CompanyApi;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface CompanyService {
  CompanyApi save(CompanyApi api);
  Optional<CompanyApi> update(CompanyApi api);
  Optional<CompanyApi> findByUuid(UUID uuid);
  Page<CompanyApi> list(int page, int maxItems);
  Page<CompanyApi> list(int page, int maxItems, Sort sort);
  void delete(UUID companyUuid);
}
