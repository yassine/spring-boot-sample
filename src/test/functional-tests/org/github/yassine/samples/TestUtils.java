package org.github.yassine.samples;

import static com.google.common.collect.ImmutableMap.builder;
import static java.lang.String.format;
import static java.util.Collections.singletonList;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.PortBinding;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

public class TestUtils {

  private final static String AUTH_REALM_NAME = "test-realm";
  private final static String AUTH_CLIENT_NAME = "test-client";
  private final static String AUTH_USER_NAME = "dev-user";
  private final static String AUTH_USER_PASS = "dev-user";
  private final static Integer AUTH_PORT = 8090;
  private final static String  AUTH_HOST = "localhost";
  private final static Integer APP_POSTGRES_PORT = 15432;

  @SneakyThrows
  public static String initKeycloak(){
    addUsers();
    Keycloak adminKeycloak = Keycloak
      .getInstance("http://localhost:8090/auth","master", "admin", "admin", "admin-cli");
    adminKeycloak.realms().realm("test-realm").keys().getKeyMetadata().getKeys().forEach(key -> {
      if(key.getPublicKey() != null){
        System.setProperty("keycloak.deployment.publicKey", key.getPublicKey());
      }
    });
    return getAccessToken();
  }


  public static TestRule testRule(){
    Network network = Network.newNetwork();

    GenericContainer keycloakDb = new PostgreSQLContainer("postgres:9.6-alpine")
      .withUsername("dev")
      .withPassword("dev")
      .withDatabaseName("keycloak-db")
      .withNetwork(network);

    keycloakDb.start();

    GenericContainer keycloak = new GenericContainer(new ImageFromDockerfile()
      .withFileFromClasspath("Dockerfile","docker/keycloak/Dockerfile")
      .withFileFromClasspath("module.xml","docker/keycloak/module.xml")
      .withFileFromClasspath("transform.xsl","docker/keycloak/transform.xsl")
      .withFileFromClasspath("import-realm.json","docker/keycloak/import-realm.json")
      ).withEnv(
        builder()
          .put("POSTGRES_PORT_5432_TCP_ADDR", keycloakDb.getContainerInfo().getNetworkSettings().getNetworks().entrySet().stream().findAny()
            .map(entry -> entry.getValue().getIpAddress())
            .get()
          )
          .put("POSTGRES_USER", "dev")
          .put("POSTGRES_PASSWORD", "dev")
          .put("POSTGRES_DATABASE", "keycloak-db")
          .put("KEYCLOAK_USER", "admin")
          .put("KEYCLOAK_PASSWORD", "admin")
          .build()
      )
      .withNetwork(network)
      .withExposedPorts(8080)
      .withCreateContainerCmdModifier(
        cmd -> ((CreateContainerCmd) cmd).withPortBindings(PortBinding.parse("8090:8080"))
      );


    GenericContainer app_postgres_db = new PostgreSQLContainer("postgres:9.6-alpine")
      .withUsername("dev")
      .withPassword("dev")
      .withDatabaseName("spring_boot_sample_db")
      .withExposedPorts(APP_POSTGRES_PORT, 5432)
      .withCreateContainerCmdModifier(
        cmd -> ((CreateContainerCmd) cmd).withPortBindings(PortBinding.parse(format("%s:%s", APP_POSTGRES_PORT, 5432)))
      );

    return RuleChain.outerRule(keycloak)
            .around(app_postgres_db);
  }

  @SneakyThrows
  private static void addUsers(){

    Keycloak adminKeycloak = Keycloak
      .getInstance("http://localhost:8090/auth","master", "admin", "admin", "admin-cli");

    ClientRepresentation clientRepresentation = new ClientRepresentation();
    clientRepresentation.setId(AUTH_CLIENT_NAME);
    clientRepresentation.setName(AUTH_CLIENT_NAME);
    clientRepresentation.setProtocol("openid-connect");
    clientRepresentation.setConsentRequired(false);
    adminKeycloak.realms().realm(AUTH_REALM_NAME).clients().create(clientRepresentation);

    CredentialRepresentation cr = new CredentialRepresentation();
    cr.setType("password");
    cr.setValue(AUTH_USER_PASS);
    cr.setTemporary(false);
    UserRepresentation userRepresentation = new UserRepresentation();
    userRepresentation.setUsername(AUTH_USER_NAME);
    userRepresentation.setCredentials(singletonList(cr));
    userRepresentation.setEmailVerified(true);
    userRepresentation.setEnabled(true);
    adminKeycloak.realms().realm(AUTH_REALM_NAME).users()
      .create(userRepresentation);
  }

  @SneakyThrows
  public static String getAccessToken(){
    return JsonPath.read(Configuration.defaultConfiguration().jsonProvider().parse(
      new OkHttpClient()
            .newCall(new Request.Builder()
              .url(format("http://%s:%s/auth/realms/%s/protocol/openid-connect/token", AUTH_HOST, AUTH_PORT, AUTH_REALM_NAME))
              .header("Content-Type", "application/x-www-form-urlencoded")
              .post(new FormBody.Builder()
                .add("username", AUTH_USER_NAME)
                .add("password", AUTH_USER_PASS)
                .add("grant_type", "password")
                .add("client_id", AUTH_CLIENT_NAME)
                .build())
              .build()).execute().body().string()), "$.access_token");
  }

}
