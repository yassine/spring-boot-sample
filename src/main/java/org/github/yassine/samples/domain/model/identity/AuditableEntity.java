package org.github.yassine.samples.domain.model.identity;

import java.time.Instant;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.github.yassine.samples.domain.model.shared.UUIDIdentifiable;
import org.hibernate.annotations.GenericGenerator;

@MappedSuperclass
@Getter
@Setter
@Accessors(chain = true)
public class AuditableEntity extends UUIDIdentifiable {
  @GeneratedValue(generator = "sequence-per-table")
  @Id
  @GenericGenerator(
    name = "sequence-per-table",
    strategy = "org.github.yassine.samples.core.PlatformSequenceGeneratorStrategy")
  private Long id;
  private Instant created;
  @ManyToOne(fetch = FetchType.LAZY)
  private Identity createdBy;
  private Instant lastModified;
  @ManyToOne(fetch = FetchType.LAZY)
  private Identity lastModifiedBy;
  @ManyToOne(fetch = FetchType.LAZY)
  private Identity tenant;
}
