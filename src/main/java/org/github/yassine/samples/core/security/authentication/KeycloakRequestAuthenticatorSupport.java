package org.github.yassine.samples.core.security.authentication;

import static com.machinezoo.noexception.Exceptions.sneak;
import static java.util.Collections.list;
import static org.keycloak.RSATokenVerifier.verifyToken;

import io.reactivex.Maybe;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.keycloak.adapters.KeycloakDeployment;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
@Slf4j
public class KeycloakRequestAuthenticatorSupport implements RequestAuthenticator {

  private static final String SPACE_PATTERN = "\\s+";
  private static final String BEARER_HEADER = "Bearer";
  private static final String AUTHORIZATION_HEADER = "Authorization";

  private final KeycloakDeployment keycloakDeployment;

  @Override
  public Maybe<AuthenticationToken> authenticate(ServletRequest servletRequest) {
    if(servletRequest instanceof HttpServletRequest){
      return extractAccessToken((HttpServletRequest) servletRequest);
    }
    return Maybe.empty();
  }

  private Maybe<AuthenticationToken> extractAccessToken(HttpServletRequest request){
    return list(request.getHeaders(AUTHORIZATION_HEADER)).stream()
      .map(header -> header.trim().split(SPACE_PATTERN))
      .filter(parts -> parts.length == 2)
      .filter(parts -> parts[0].equalsIgnoreCase(BEARER_HEADER))
      .map(parts -> parts[1])
      .findAny()
      .map(Maybe::just)
      .orElse(Maybe.empty())
      .map( token -> sneak().get(() ->
        verifyToken(token,
          keycloakDeployment.getPublicKeyLocator().getPublicKey("", null),
          keycloakDeployment.getRealmInfoUrl())
        )
      )
      .map(KeycloakAuthenticationToken::new);
  }

}
