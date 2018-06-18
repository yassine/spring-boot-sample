package org.github.yassine.samples.core.mapping;

import static org.github.yassine.samples.core.utils.ScanUtils.scanAndInstantiateImplementations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFactory;

@RequiredArgsConstructor(staticName = "of", access = AccessLevel.PRIVATE)
@Getter
class Orika {

  private final Set<Converter> converters;
  private final Set<MapperConfigurationPlugin> plugins;

  static class Builder {

    private final Set<MapperConfigurationPlugin> plugins = Sets.newHashSet();
    private final Set<Converter> converters = Sets.newHashSet();
    private final List<String> autoDiscoverPackages = Lists.newArrayList();

    public Builder registerDefaultMapper(Class<?> from, Class<?> to) {
      plugins.add(DefaultMapperConfigurationPlugin.of(from, to));
      return this;
    }

    public Builder register(MapperConfigurationPlugin mappingPlugin) {
      plugins.add(mappingPlugin);
      return this;
    }

    public Builder registerConverter(Converter converter) {
      converters.add(converter);
      return this;
    }

    public Builder autoDiscover(String... packages) {
      autoDiscoverPackages.addAll(Arrays.asList(packages));
      return this;
    }

    public Orika build() {
      autoDiscoverPackages.stream()
        .map((packageName) ->
          scanAndInstantiateImplementations(Converter.class, packageName))
        .flatMap(Set::stream)
        .forEach(converters::add);
      autoDiscoverPackages.stream()
        .map((packageName) ->
          scanAndInstantiateImplementations(MapperConfigurationPlugin.class, packageName))
        .flatMap(Set::stream)
        .forEach(plugins::add);
      return Orika.of(converters, plugins);
    }

  }

  @RequiredArgsConstructor(staticName = "of")
  static class DefaultMapperConfigurationPlugin implements MapperConfigurationPlugin {
    private final Class<?> from;
    private final Class<?> to;

    @Override
    public void accept(MapperFactory t) {
      t.classMap(from, to)
        .byDefault()
        .register();
    }
  }

}
