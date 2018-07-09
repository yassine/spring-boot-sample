package org.github.yassine.samples.core.security;

import static org.github.yassine.samples.core.utils.BindingUtils.bindMultiContractImplementation;

import com.google.common.collect.ImmutableSet;
import org.apache.shiro.realm.Realm;
import org.github.yassine.samples.core.security.authentication.KeycloakAuthenticatingRealm;
import org.github.yassine.samples.core.security.authentication.KeycloakRequestAuthenticatorSupport;
import org.github.yassine.samples.core.security.authentication.RequestAuthenticator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.ParameterizedTypeReference;

public class SecurityPostBeanProcessor implements BeanDefinitionRegistryPostProcessor {

  private ConfigurableListableBeanFactory beanFactory;

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    bindMultiContractImplementation(
      registry,
      RequestAuthenticator.class,
      (new ParameterizedTypeReference<RequestAuthenticator>(){}).getType(),
      ImmutableSet.of(KeycloakRequestAuthenticatorSupport.class),
      () -> beanFactory
    );
    bindMultiContractImplementation(
      registry,
      Realm.class,
      (new ParameterizedTypeReference<Realm>(){}).getType(),
      ImmutableSet.of(KeycloakAuthenticatingRealm.class),
      () -> beanFactory
    );
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }
}
