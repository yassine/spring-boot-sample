package org.github.yassine.samples.cli;

import static org.github.yassine.samples.cli.CliUtils.liquibase;

import io.airlift.airline.Command;
import lombok.extern.slf4j.Slf4j;

@Command(name = "init-data")
@Slf4j
public class DbInitCommand extends ConfigurableCommand {
  @Override
  public void run() {
    liquibase("liquibase/init-data/_changelog.yaml", configPath);
  }
}
