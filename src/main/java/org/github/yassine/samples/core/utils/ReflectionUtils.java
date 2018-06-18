package org.github.yassine.samples.core.utils;

import static com.google.common.base.Throwables.throwIfUnchecked;

import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
class ReflectionUtils {

  public static Object instantiate(Class<?> clazz) {
    try {
      return clazz.newInstance();
    } catch (Exception e) {
      throwIfUnchecked(e);
      throw new RuntimeException(e);
    }
  }

  public static boolean hasNoArgConstructor(Class clazz) {
    return Arrays.stream(clazz.getDeclaredConstructors())
      .anyMatch((constructor) -> constructor.getParameterCount() == 0);
  }


}
