package org.github.yassine.samples.domain.model.company;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;
import org.github.yassine.samples.domain.model.identity.AuditableEntity;
import org.github.yassine.samples.domain.model.shared.AddressInformation;

@Getter
@Setter
@Entity
public class Company extends AuditableEntity {

  private String name;
  @Valid
  private AddressInformation address;
  @Email
  private String email;
  private String phone;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "rel__company__person",
    joinColumns = @JoinColumn(name = "company_id"),
    inverseJoinColumns = @JoinColumn(name = "person_id"))
  private List<Person> owners;

}
