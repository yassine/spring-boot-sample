package org.github.yassine.samples.domain.model;

public interface Entity<T> {
  default boolean sameAs(T valueObject) {
    return equals(valueObject);
  }
}
