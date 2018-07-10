package org.github.yassine.samples.cli;

import static java.util.Optional.ofNullable;

import com.machinezoo.noexception.Exceptions;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;
import java.util.Properties;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j @UtilityClass
class CliUtils {

  static Properties getProps(String path) {
    return ofNullable(path)
      .map(p -> Exceptions.sneak().get(() -> (InputStream) new FileInputStream(p)))
      .map(Optional::of)
      .orElseGet(() -> ofNullable(CliUtils.class.getResourceAsStream("/application.properties")))
      .map(CliUtils::load)
      .orElse(new Properties());
  }

  @SneakyThrows
  static Properties load(InputStream is) {
    Properties props = new Properties();
    props.load(is);
    return props;
  }

  static void liquibase(String changelogFile, String configPath) {
    try (Connection connection = CliUtils.getConnection(configPath)) {
      ResourceAccessor ra = new ClassLoaderResourceAccessor(CliUtils.class.getClassLoader());
      JdbcConnection jdbcConnection = new JdbcConnection(connection);
      Liquibase liquibase = new Liquibase(changelogFile, ra, jdbcConnection);
      liquibase.update((Contexts) null);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  @SneakyThrows
  static Connection getConnection(String configPath) {
    Properties props = getProps(configPath);
    return DriverManager.getConnection(getJdbcUrl(props),
      getJdbcUser(props), getJdbcPassword(props));
  }

  static String getJdbcUrl(Properties properties) {
    return properties.getProperty("spring.datasource.url");
  }

  static String getJdbcUser(Properties properties) {
    return properties.getProperty("spring.datasource.username");
  }

  static String getJdbcPassword(Properties properties) {
    return properties.getProperty("spring.datasource.password");
  }
}
