package org.github.yassine.samples.core.security.authorization;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.github.yassine.samples.domain.model.identity.Identity;
import org.github.yassine.samples.domain.model.identity.IdentityRole;

public class RoleAuthorizationRealm extends AuthorizingRealm {

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    return new AuthorizationInfo() {
      @Override
      public Collection<String> getRoles() {
        return Optional.of(principals.oneByType(Identity.class))
                .map(identity -> identity.getRoles().values().stream().map(IdentityRole::getRole)
                  .map(Role::toString)
                  .collect(Collectors.toSet()))
                .orElse(ImmutableSet.of());
      }

      @Override
      public Collection<String> getStringPermissions() {
        return getRoles().stream().map(Role::valueOf)
                .map(Role::getPermissions)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
      }

      @Override
      public Collection<Permission> getObjectPermissions() {
        return new ArrayList<>();
      }
    };
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
    return null;
  }
}
