package org.github.yassine.samples.domain.event.company;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCreatedEvent {
  private String name;
}
