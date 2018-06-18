package org.github.yassine.samples.core;

import java.util.Properties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

public class PlatformSequenceGeneratorStrategy extends SequenceStyleGenerator {
  @Getter
  @Setter
  private Dialect dialect;

  @Override
  protected QualifiedName determineSequenceName(Properties params, Dialect dialect,
                                                JdbcEnvironment jdbcEnv) {
    return QualifiedNameParser.INSTANCE.parse(
      "seq__" + params.get(PersistentIdentifierGenerator.TABLE) + "__id");
  }

}
