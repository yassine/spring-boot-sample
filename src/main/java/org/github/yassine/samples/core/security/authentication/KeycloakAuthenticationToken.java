package org.github.yassine.samples.core.security.authentication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationToken;
import org.keycloak.representations.AccessToken;

@RequiredArgsConstructor
public class KeycloakAuthenticationToken implements AuthenticationToken {
  @Getter
  private final AccessToken accessToken;

  @Override
  public Object getPrincipal() {
    return accessToken.getSubject();
  }

  @Override
  public Object getCredentials() {
    return accessToken.getSubject();
  }
}
