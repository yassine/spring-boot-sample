package org.github.yassine.samples.cli;

import static org.github.yassine.samples.cli.CliUtils.liquibase;

import io.airlift.airline.Command;

@Command(name = "install")
public class DbInstallCommand extends ConfigurableCommand {
  @Override
  public void run() {
    liquibase("liquibase/install/_changelog.yaml", configPath);
  }
}
