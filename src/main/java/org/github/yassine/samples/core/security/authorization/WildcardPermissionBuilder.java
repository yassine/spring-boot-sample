package org.github.yassine.samples.core.security.authorization;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableSet.copyOf;
import static com.google.common.collect.Sets.union;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WildcardPermissionBuilder {

  private static final String CONTEXT_SEPARATOR = ":";
  private static final String SEPARATOR = ",";
  private static final String WILD_CARD = "*";

  private WildcardPermissionBuilder parent;

  private WildcardPermissionBuilder(WildcardPermissionBuilder parent) {
    this.parent = parent;
  }

  private Set<Action> actions   = new HashSet<>();
  private Set<Resource> resources = new HashSet<>();
  private Set<String> scopes    = new HashSet<>();

  public WildcardPermissionBuilder allow(Action... actions) {
    this.actions.addAll(Arrays.asList(actions));
    return this;
  }

  public WildcardPermissionBuilder allowAnyAction() {
    this.actions = Collections.singleton(Action.ANY);
    return this;
  }

  public WildcardPermissionBuilder inScopes(String... scopes) {
    this.scopes.addAll(Arrays.asList(scopes));
    return this;
  }

  public WildcardPermissionBuilder inAnyScope() {
    scopes = Collections.singleton(WILD_CARD);
    return this;
  }

  public WildcardPermissionBuilder onResources(Resource... resources) {
    this.resources.addAll(Arrays.asList(resources));
    return this;
  }

  public WildcardPermissionBuilder onAnyResource() {
    this.resources = Collections.singleton(Resource.ANY);
    return this;
  }

  public WildcardPermissionBuilder and() {
    return new WildcardPermissionBuilder(this);
  }

  /**
   * Build the list of constructed permissions.
   * @return the list of permissions
   */
  public Set<String> build() {
    checkState( !resources.isEmpty(),
        "At least one resource is required. Permission : %s", getStringPermission());
    checkState( !scopes.isEmpty(),
        "At least one scope is required. Permission : %s", getStringPermission());
    checkState( !actions.isEmpty(),
        "At least one action is required. Permission : %s", getStringPermission());
    if (parent == null) {
      return build(new HashSet<>());
    } else {
      return build(parent.build());
    }
  }

  private Set<String> build(Set<String> parentPermissions) {
    return copyOf(union(
              ImmutableSet.of(getStringPermission()),
              parentPermissions
            ));
  }

  private String getStringPermission() {
    return Joiner.on(CONTEXT_SEPARATOR)
            .join(Joiner.on(SEPARATOR).join(resources),
              Joiner.on(SEPARATOR).join(scopes),
              Joiner.on(SEPARATOR).join(actions));
  }

  public static WildcardPermissionBuilder builder() {
    return new WildcardPermissionBuilder();
  }

}
