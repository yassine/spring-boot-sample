package org.github.yassine.samples.core.security;

import static java.util.Optional.ofNullable;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
@Order(510)
public class RouteFirewallFilter implements Filter {

  private final FilterChainResolver filterChainResolver;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {

  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    ofNullable(filterChainResolver.getChain(servletRequest, servletResponse, filterChain))
      .orElse(filterChain)
      .doFilter(servletRequest, servletResponse);
  }

  @Override
  public void destroy() {

  }
}
