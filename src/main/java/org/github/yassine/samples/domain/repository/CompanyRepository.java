package org.github.yassine.samples.domain.repository;

import java.util.UUID;
import org.github.yassine.samples.domain.model.company.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends CrudRepository<Company, Long> {

  Company findByUuid(UUID uuid);

}
