package org.github.yassine.samples.cli;

import io.airlift.airline.Option;

public abstract class ConfigurableCommand implements Runnable {
  @Option(name = "-c", description = "The path to an overrides props file")
  public String configPath;
}
