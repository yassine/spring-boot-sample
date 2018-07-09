package org.github.yassine.samples;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

  public static void main(String... args) {
    new ApplicationCli().run(args);
  }

}
