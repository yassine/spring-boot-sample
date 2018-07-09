package org.github.yassine.samples.core.security.authentication;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.github.yassine.samples.domain.model.identity.Identity;
import org.github.yassine.samples.domain.model.identity.IdentityProvider;
import org.github.yassine.samples.domain.repository.IdentityProviderRepository;
import org.github.yassine.samples.domain.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
public class KeycloakAuthenticatingRealm extends AuthenticatingRealm {

  private final static String NAME = "KEYCLOAK";

  private final IdentityRepository identityRepository;
  private final IdentityProviderRepository identityProviderRepository;

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) token;
    SimpleAuthenticationInfo authInfo    = new SimpleAuthenticationInfo();
    SimplePrincipalCollection principals = new SimplePrincipalCollection();
    principals.add(keycloakAuthenticationToken, NAME);
    principals.add(keycloakAuthenticationToken.getAccessToken(), NAME);
    authInfo.setPrincipals(principals);
    authInfo.setCredentials(keycloakAuthenticationToken.getCredentials());
    IdentityProvider identityProvider = identityProviderRepository.findByName(NAME);
    Identity identity = identityRepository.findByExternalIdAndProvider(keycloakAuthenticationToken.getAccessToken().getSubject(), identityProvider);
    if(identity == null){
      identity = new Identity();
      identity.setProvider(identityProvider);
      identity.setExternalId(keycloakAuthenticationToken.getAccessToken().getSubject());
      identityRepository.save(identity);
    }
    principals.add(identity, NAME);
    return authInfo;
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof KeycloakAuthenticationToken;
  }
}
