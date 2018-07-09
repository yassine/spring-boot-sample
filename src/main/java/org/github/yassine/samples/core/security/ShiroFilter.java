package org.github.yassine.samples.core.security;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor(onConstructor = @__(@Autowired), access = AccessLevel.PUBLIC)
public class ShiroFilter extends AbstractShiroFilter {

  private final WebSecurityManager webSecurityManager;


  @Override
  public void init() throws Exception {
    setSecurityManager(webSecurityManager);
  }
}
