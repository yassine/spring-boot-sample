package org.github.yassine.samples.core.security;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

class Filters {
  static final Filter AUTHENTICATED = new AuthenticationFilter() {
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
      ((HttpServletResponse) response).setStatus(401);
      return false;
    }
  };
}
