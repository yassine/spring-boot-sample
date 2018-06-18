package org.github.yassine.samples.domain.repository;

import org.github.yassine.samples.domain.model.company.Company;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long>, DomainRepository<Company> {

}
