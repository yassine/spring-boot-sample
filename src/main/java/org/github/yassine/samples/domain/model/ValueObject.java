package org.github.yassine.samples.domain.model;

public interface ValueObject<T> {
  default boolean sameAs(T valueObject) {
    return equals(valueObject);
  }
}
