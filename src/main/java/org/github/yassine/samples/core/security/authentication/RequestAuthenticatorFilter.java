package org.github.yassine.samples.core.security.authentication;

import static io.reactivex.Observable.fromIterable;

import java.io.IOException;
import java.util.Set;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
@Order(500)
public class RequestAuthenticatorFilter implements Filter {

  private final Set<RequestAuthenticator> requestAuthenticator;

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    fromIterable(requestAuthenticator)
      .flatMapMaybe(requestAuthenticator -> requestAuthenticator.authenticate(servletRequest).onErrorComplete())
      .blockingSubscribe(SecurityUtils.getSubject()::login);
    filterChain.doFilter(servletRequest, servletResponse);
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {}
  @Override
  public void destroy() {}

}
