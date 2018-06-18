package org.github.yassine.samples.cli;

import io.airlift.airline.Command;
import java.util.Properties;
import org.github.yassine.samples.Application;
import org.springframework.boot.SpringApplication;

@Command(name = "serve", description = "bootstrap the spring boot application")
public class ServiceCommand extends ConfigurableCommand {
  @Override
  public void run() {
    Properties props = CliUtils.getProps(configPath);
    props.stringPropertyNames()
      .forEach(name -> System.setProperty(name, props.getProperty(name)));
    SpringApplication.run(Application.class);
  }
}
