package org.github.yassine.samples.core.security.authorization;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Action {
  READ("READ"),
  CREATE("CREATE"),
  UPDATE("UPDATE"),
  DELETE("DELETE"),
  ANY("*");

  private final String name;

  public String toString(){
    return name;
  }

}
