package org.github.yassine.samples.core.mapping;

import com.google.common.base.Joiner;
import java.util.Arrays;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.bytecode.SignatureAttribute;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.BoundMapperFacade;
import org.github.yassine.samples.Application;
import org.github.yassine.samples.domain.model.shared.UUIDIdentifiable;
import org.github.yassine.samples.dto.IdentifiableApi;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

@SuppressWarnings("NullableProblems")
@Slf4j
public class MappingBeanPostProducer implements BeanDefinitionRegistryPostProcessor {

  private static final String BOUND_MAPPER_SUFFIX = "BoundMapper";
  private final Orika orika = new Orika.Builder()
    .autoDiscover(Application.class.getPackage().getName())
    .register(mpf ->
      mpf.classMap(UUIDIdentifiable.class, IdentifiableApi.class)
        .fieldAToB("uuid", "id")
        .register()
    )
    .build();
  private final DefaultMapperFactoryArtifact mapperFactory = new DefaultMapperFactoryArtifact.Builder().build();

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {

    final ClassPool defaultClassPool = ClassPool.getDefault();
    orika.getPlugins().forEach(plugin -> plugin.accept(mapperFactory));
    orika.getConverters().forEach(plugin -> mapperFactory.getConverterFactory().registerConverter(plugin));

    mapperFactory.getClassMapRegistry().keySet().forEach(key -> {

      // This (hack) can be externalized to a Utility class
      // IMHO this is an aspect where Guice beats Spring (Guice has stronger/friendlier programmatic type binding)

      final BoundMapperFacade<?, ?> boundMapperFacade = mapperFactory.getMapperFacade(key.getAType().getRawType(), key.getBType().getRawType());
      final BoundMapperFacade<?, ?> reverseBoundMapperFacade = mapperFactory.getMapperFacade(key.getBType().getRawType(), key.getAType().getRawType());

      try {
        CtClass boundMapperCtClass = defaultClassPool.getCtClass(BoundMapperFacade.class.getName());

        String directName = Joiner.on("").join(getClass().getPackage().getName(), ".",
          key.getAType().getSimpleName(), key.getBType().getSimpleName(), BOUND_MAPPER_SUFFIX);
        String reverseName = Joiner.on("").join(getClass().getPackage().getName(), ".",
          key.getBType().getSimpleName(), key.getAType().getSimpleName(), BOUND_MAPPER_SUFFIX);

        CtClass directMapperInterface = defaultClassPool.makeInterface(directName, boundMapperCtClass);
        CtClass reverseMapperInterface = defaultClassPool.makeInterface(reverseName, boundMapperCtClass);

        SignatureAttribute directSignatureAttribute = new SignatureAttribute(
          directMapperInterface.getClassFile().getConstPool(),
          getParametrizedTypeSignature(BoundMapperFacade.class, key.getAType().getRawType(),
            key.getBType().getRawType()));

        SignatureAttribute reverseSignatureAttribute = new SignatureAttribute(
          reverseMapperInterface.getClassFile().getConstPool(),
          getParametrizedTypeSignature(BoundMapperFacade.class, key.getBType().getRawType(),
            key.getAType().getRawType()));

        directMapperInterface.getClassFile().addAttribute(directSignatureAttribute);
        reverseMapperInterface.getClassFile().addAttribute(reverseSignatureAttribute);

        GenericBeanDefinition directBoundMapperBeanDefinition = new GenericBeanDefinition();
        Class directBoundMapperClass = directMapperInterface.toClass();
        directBoundMapperBeanDefinition.setBeanClass(directBoundMapperClass);
        directBoundMapperBeanDefinition.setInstanceSupplier(() -> boundMapperFacade);

        GenericBeanDefinition reverseBoundMapperBeanDefinition = new GenericBeanDefinition();
        Class reverseBoundMapperClass = reverseMapperInterface.toClass();
        reverseBoundMapperBeanDefinition.setBeanClass(reverseBoundMapperClass);
        reverseBoundMapperBeanDefinition.setInstanceSupplier(() -> reverseBoundMapperFacade);

        registry.registerBeanDefinition(directName, directBoundMapperBeanDefinition);
        registry.registerBeanDefinition(reverseName, reverseBoundMapperBeanDefinition);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }

    });
  }


  private String getParametrizedTypeSignature(Class type, Class... params) {
    return String.format("Ljava/lang/Object;L%s<%s>;", type.getName().replaceAll("\\.", "/"),
      Arrays.stream(params)
        .map(Class::getName)
        .map(name -> "L" + name.replaceAll("\\.", "/") + ";")
        .reduce("", (a, b) -> StringUtils.isEmpty(a) ? b : Joiner.on("").join(a, b)));
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    orika.getConverters().forEach(beanFactory::autowireBean);
    orika.getPlugins().forEach(beanFactory::autowireBean);
  }

}