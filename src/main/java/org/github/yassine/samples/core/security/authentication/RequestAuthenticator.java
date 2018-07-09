package org.github.yassine.samples.core.security.authentication;

import io.reactivex.Maybe;
import javax.servlet.ServletRequest;
import org.apache.shiro.authc.AuthenticationToken;

public interface RequestAuthenticator {
  Maybe<AuthenticationToken> authenticate(ServletRequest httpServletRequest);
}
