package org.github.yassine.samples.core.security;

import static org.github.yassine.samples.core.security.Filters.AUTHENTICATED;

import java.util.Set;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.github.yassine.samples.core.security.authentication.KeycloakDeploymentConfiguration;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.KeycloakDeploymentBuilder;
import org.keycloak.representations.adapters.config.AdapterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({SecurityPostBeanProcessor.class, KeycloakDeploymentConfiguration.class})
public class SecurityConfiguration {

  @Bean
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  WebSecurityManager webSecurityManager(Set<Realm> realms){
    return new StatelessSecurityManager(realms);
  }

  @Bean
  KeycloakDeployment keycloakDeployment(KeycloakDeploymentConfiguration keycloakDeploymentConfiguration){
    AdapterConfig adapterConfig = new AdapterConfig();
    adapterConfig.setRealm(keycloakDeploymentConfiguration.getRealm());
    adapterConfig.setResource(keycloakDeploymentConfiguration.getResource());
    adapterConfig.setRealmKey(keycloakDeploymentConfiguration.getPublicKey());
    adapterConfig.setAuthServerUrl(keycloakDeploymentConfiguration.getAuthServerUrl());
    adapterConfig.setPublicClient(true);
    return KeycloakDeploymentBuilder.build(adapterConfig);
  }

  @Bean
  FilterChainResolver getFilterChainResolver(){
    PathMatchingFilterChainResolver resolver = new PathMatchingFilterChainResolver();
    resolver.getFilterChainManager().addFilter("authenticated", AUTHENTICATED);
    resolver.getFilterChainManager().addToChain("/**","authenticated");
    return resolver;
  }

  @Bean
  AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(WebSecurityManager securityManager){
    AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
    advisor.setSecurityManager(securityManager);
    return advisor;
  }

}
