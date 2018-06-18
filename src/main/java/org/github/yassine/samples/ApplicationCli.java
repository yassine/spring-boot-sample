package org.github.yassine.samples;

import io.airlift.airline.Cli;
import io.airlift.airline.Help;
import org.github.yassine.samples.cli.DbInitCommand;
import org.github.yassine.samples.cli.DbInstallCommand;
import org.github.yassine.samples.cli.ServiceCommand;

public class ApplicationCli {

  public void run(String... args) {
    Cli.CliBuilder<Runnable> cliBuilder = new Cli.CliBuilder<Runnable>("spring-sample-app")
      .withDescription("A demo application backed by spring boot")
      .withDefaultCommand(Help.class)
      .withCommands(ServiceCommand.class, Help.class);
    cliBuilder
      .withGroup("db")
      .withDescription("Database versioning related commands")
      .withCommands(DbInstallCommand.class, DbInitCommand.class);
    cliBuilder.build().parse(args).run();
  }

}
