package org.github.yassine.samples.core.graphql;

import java.util.UUID;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.github.yassine.samples.api.model.CompanyApi;
import org.github.yassine.samples.api.model.PersonApi;
import org.github.yassine.samples.domain.service.CompanyService;
import org.github.yassine.samples.domain.service.PersonService;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor(onConstructor = @__(@Inject), access = AccessLevel.PUBLIC)
@Component
public class GraphQLQueryService {

  private final PersonService personService;
  private final CompanyService companyService;

  public PersonApi person(UUID uuid){
    return personService.findByUUID(uuid).orElse(null);
  }

  public CompanyApi company(UUID uuid){
    return companyService.findByUuid(uuid).orElse(null);
  }

}
