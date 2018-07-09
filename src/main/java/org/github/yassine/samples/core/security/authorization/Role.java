package org.github.yassine.samples.core.security.authorization;

import static org.github.yassine.samples.core.security.authorization.WildcardPermissionBuilder.builder;

import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {

  SYSTEM(builder()
          .allowAnyAction().onAnyResource().inAnyScope()
        .build()),

  ADMIN(builder()
    .allowAnyAction().onAnyResource().inAnyScope()
    .build()
  ),

  USER(
    builder()
      .allow(Action.READ)
      .onResources(Resource.COMPANY)
      .inAnyScope()
      .build()
  );
  @Getter
  private final Set<String> permission;
}
