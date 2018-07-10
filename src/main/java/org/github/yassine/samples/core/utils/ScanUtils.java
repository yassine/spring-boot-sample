package org.github.yassine.samples.core.utils;

import com.google.common.collect.ImmutableSet;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ScanUtils {

  @SuppressWarnings("unchecked")
  public static <C, I extends C> Set<Class<I>> scanForImplementationsOf(Class<C> clazz,
                                                                        String... packages) {
    ImmutableSet.Builder<Class<I>> items = ImmutableSet.builder();
    FastClasspathScanner scanner = new FastClasspathScanner(packages);
    scanner.matchClassesImplementing(clazz, impl -> items.add((Class<I>) impl));
    scanner.scan();
    return items.build();
  }

  @SuppressWarnings("unchecked")
  public static <C, I extends C> Set<I> scanAndInstantiateImplementations(Class<C> contractClass,
                                                                          String... packages) {
    return ImmutableSet.copyOf(scanForImplementationsOf(contractClass, packages)).stream()
      .filter(ReflectionUtils::hasNoArgConstructor)
      .map( implementation -> (I) ReflectionUtils.instantiate(implementation))
      .collect(Collectors.toSet());
  }

}
