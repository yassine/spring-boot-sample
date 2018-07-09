package org.github.yassine.samples.core.security;

import java.util.Set;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.beans.factory.annotation.Autowired;

public class StatelessSecurityManager extends DefaultWebSecurityManager {

  @Autowired
  public StatelessSecurityManager(Set<Realm> realms) {
    MemoryConstrainedCacheManager cacheManager = new MemoryConstrainedCacheManager();
    setCacheManager(cacheManager);
    ((DefaultWebSessionStorageEvaluator)((DefaultSubjectDAO) this.subjectDAO).getSessionStorageEvaluator())
      .setSessionStorageEnabled(false);
    setRealms(realms);
  }
}
