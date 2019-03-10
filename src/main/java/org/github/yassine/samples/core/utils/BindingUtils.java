package org.github.yassine.samples.core.utils;

import static java.util.UUID.randomUUID;
import static org.github.yassine.samples.core.utils.ReflectionUtils.toByteCodeName;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.SignatureAttribute;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.reflect.TypeUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;

@UtilityClass
public class BindingUtils {

  @SneakyThrows
  public static GenericBeanDefinition bindParametrizedType(Class rawType, Type type, Supplier<?> instanceSupplier) {
    final ClassPool defaultClassPool = ClassPool.getDefault();
    String generatedClassName = getName(rawType);
    defaultClassPool.insertClassPath(new ClassClassPath(rawType));
    CtClass contractCtClass = defaultClassPool.getCtClass(rawType.getName());
    CtClass generatedCtClass = rawType.isInterface()
      ? defaultClassPool.makeInterface(generatedClassName, contractCtClass)
      : defaultClassPool.makeClass(generatedClassName, contractCtClass);
    SignatureAttribute signatureAttribute = new SignatureAttribute(
      generatedCtClass.getClassFile().getConstPool(),
      toByteCodeName(type));
    generatedCtClass.getClassFile().addAttribute(signatureAttribute);
    GenericBeanDefinition typeBeanDefinition = new GenericBeanDefinition();
    Class generatedTypeClass = generatedCtClass.toClass();
    typeBeanDefinition.setBeanClass(generatedTypeClass);
    typeBeanDefinition.setInstanceSupplier(instanceSupplier);
    return typeBeanDefinition;
  }

  public static <T> void bindMultiContractImplementation(BeanDefinitionRegistry beanDefinitionRegistry, Class<T> contractRawType, Type contract, Set<Class<? extends T>> implementations, Supplier<ConfigurableBeanFactory> supplier){
    implementations.stream()
      .forEach(definition -> {
        GenericBeanDefinition typeBeanDefinition = new GenericBeanDefinition();
        typeBeanDefinition.setBeanClass(definition);
        typeBeanDefinition.setAutowireCandidate(false);
        beanDefinitionRegistry.registerBeanDefinition(implementationBeanName(contractRawType, definition), typeBeanDefinition);
      });
    BeanDefinition definition = bindParametrizedType(Set.class,
      TypeUtils.parameterize(Set.class, contract)
      , new PluginSetSupplier(contractRawType, implementations, supplier));
    beanDefinitionRegistry.registerBeanDefinition(getName(ImmutableSet.class), definition);
  }


  private static String getName(Class rawType) {
    return String.format("generated.%s$Generated%s",
      rawType.getName(),
      randomUUID().toString().substring(0, 7));
  }

  private static String implementationBeanName(Class contract, Class beanClass){
    return String.format("%s-%s", contract.getName(), beanClass.getName());
  }

  @RequiredArgsConstructor
  public static class PluginSetSupplier<T> implements Supplier<ImmutableSet<T>>{

    private final Class<T> contract;
    private final Set<Class<? extends T>> implementations;
    private final Supplier<ConfigurableBeanFactory> configurableBeanFactory;
    private static final String KEY = "";

    private final LoadingCache<String, Set<T>> cache = CacheBuilder.newBuilder().build(new CacheLoader<String, Set<T>>() {
      @SuppressWarnings("unchecked")
      @Override
      public Set<T> load(String s) throws Exception {
      return implementations.stream()
        .map(name -> implementationBeanName(contract, name))
        .map(name -> (T) configurableBeanFactory.get().getBean(name))
        .collect(Collectors.toSet());
      }
    });

    @Override @SneakyThrows
    public ImmutableSet<T> get() {
      return ImmutableSet.copyOf(cache.get(KEY));
    }

  }


}
