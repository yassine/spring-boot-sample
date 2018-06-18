package org.github.yassine.samples.core;

import com.google.common.base.CaseFormat;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class PlatformDatabaseNamingStrategy extends PhysicalNamingStrategyStandardImpl {

  private static final String PATTERN = "(?:_){2,}";
  private static final String SEPARATOR = "__";

  @Override
  public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
    return canonicalize(name, context);
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
    return canonicalize(name, context);
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
    return canonicalize(name, context);
  }

  private Identifier canonicalize(Identifier name, JdbcEnvironment context) {
    return Identifier.toIdentifier(CaseFormat.UPPER_CAMEL
      .to(CaseFormat.LOWER_UNDERSCORE, name.getText())
      .replaceAll(PATTERN, SEPARATOR).toLowerCase());
  }
}
