package org.github.yassine.samples.core.security.authorization;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Resource {

  COMPANY("COMPANY"),
  ANY("*");


  private final String name;
  public String toString(){
    return name;
  }

}
