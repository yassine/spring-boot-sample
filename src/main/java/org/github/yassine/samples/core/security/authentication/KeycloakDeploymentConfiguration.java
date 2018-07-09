package org.github.yassine.samples.core.security.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak.deployment") @Getter @Setter
public class KeycloakDeploymentConfiguration {
  private String realm;
  private String resource;
  private String authServerUrl;
  private String publicKey;
  private boolean publicClient = true;
}
