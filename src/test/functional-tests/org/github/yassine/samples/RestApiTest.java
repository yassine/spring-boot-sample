package org.github.yassine.samples;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static java.nio.charset.Charset.defaultCharset;
import static java.sql.DriverManager.getConnection;
import static org.apache.commons.io.IOUtils.copy;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.github.dockerjava.api.command.CreateContainerCmd;
import com.github.dockerjava.api.model.PortBinding;
import com.google.common.io.Files;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

public class RestApiTest {

  @ClassRule
  public static GenericContainer postgres = new PostgreSQLContainer("postgres:9.6-alpine")
    .withUsername("dev")
    .withPassword("dev")
    .withDatabaseName("spring_boot_sample_db")
    .withExposedPorts(5432)
    .withCreateContainerCmdModifier(
      cmd -> ((CreateContainerCmd) cmd).withPortBindings(PortBinding.parse("5432:5432"))
    );

  static ConfigurableApplicationContext applicationContext;
  static File workDir = Files.createTempDir();
  static File config  = new File(workDir, "application-test.properties");
  static String id;

  @Test @SneakyThrows
  public void spec(){
    id = given()
      .body(IOUtils.toString(getClass().getResourceAsStream("/fixtures/create-company.json"), defaultCharset()))
      .header("Content-Type","application/json")
      .when()
      .post("/company")
      .then()
      .body("id", notNullValue())
      .body("name", equalTo("my-test-company-1"))
      .body("email", equalTo("my-test-company-1@localhost.com"))
      .body("phone", equalTo("000"))
      .body("address.streetLine", equalTo("1, some street"))
      .body("address.city", equalTo("Madrid"))
      .body("address.country", equalTo("Spain"))
      .extract()
      .path("id");

    //read
    given()
      .header("Content-Type","application/json")
      .when()
      .get("/company/"+id)
      .then()
      .body("id", equalTo(id))
      .body("name", equalTo("my-test-company-1"))
      .body("email", equalTo("my-test-company-1@localhost.com"))
      .body("phone", equalTo("000"))
      .body("address.streetLine", equalTo("1, some street"))
      .body("address.city", equalTo("Madrid"))
      .body("address.country", equalTo("Spain"));

    //update
    given()
      .header("Content-Type","application/json")
      .body(format(IOUtils.toString(getClass().getResourceAsStream("/fixtures/update-company.json"), defaultCharset()), id))
      .when()
      .put("/company")
      .then()
      .body("id", equalTo(id))
      .body("name", equalTo("update-my-test-company-1"))
      .body("email", equalTo("update-my-test-company-1@localhost.com"))
      .body("phone", equalTo("update-000"))
      .body("address.streetLine", equalTo("update-1, some street"))
      .body("address.city", equalTo("update-Madrid"))
      .body("address.country", equalTo("update-Spain"));

    //read updated
    given()
      .header("Content-Type","application/json")
      .when()
      .get("/company/"+id)
      .then()
      .statusCode(200)
      .body("id", equalTo(id))
      .body("name", equalTo("update-my-test-company-1"))
      .body("email", equalTo("update-my-test-company-1@localhost.com"))
      .body("phone", equalTo("update-000"))
      .body("address.streetLine", equalTo("update-1, some street"))
      .body("address.city", equalTo("update-Madrid"))
      .body("address.country", equalTo("update-Spain"));

    //add owner
    String ownerId = given()
      .body(IOUtils.toString(getClass().getResourceAsStream("/fixtures/create-owner.json"), defaultCharset()))
        .header("Content-Type","application/json")
      .when()
        .post("/company/"+id+"/owner")
      .then()
        .statusCode(200)
        .body("id", notNullValue())
        .body("firstName", equalTo("Mad"))
        .body("lastName", equalTo("Max"))
        .body("middleName", equalTo("5"))
      .extract()
        .path("id");

    given()
      .header("Content-Type","application/json")
      .when()
        .get("/company/"+id+"/owner")
      .then()
        .statusCode(200)
        .body("[0].id", equalTo(ownerId))
        .body("[0].firstName", equalTo("Mad"))
        .body("[0].lastName", equalTo("Max"))
        .body("[0].middleName", equalTo("5"));

    //delete
    given()
      .header("Content-Type","application/json")
      .when()
        .delete("/company/"+id)
      .then()
        .statusCode(200);

    given()
        .header("Content-Type","application/json")
      .when()
        .get("/company/"+id)
      .then()
        .body(equalTo("null"));
  }


  @BeforeClass @SneakyThrows
  public static void setup(){
    copy(RestApiTest.class.getResourceAsStream("/application-test.properties"), new FileOutputStream(config));
    Properties props = new Properties();
    props.load(RestApiTest.class.getResourceAsStream("/application-test.properties"));
    props.stringPropertyNames()
      .forEach(name -> System.setProperty(name, props.getProperty(name)));
    //await db
    await().ignoreExceptions().pollInterval(1, TimeUnit.SECONDS).atMost(20, TimeUnit.SECONDS)
      .untilAsserted( () ->
        getConnection(props.getProperty("spring.datasource.url"),"dev","dev")
      );
    //migrate
    Application.main("db", "install", "-c", config.getAbsolutePath());
    Application.main("db", "init-data", "-c", config.getAbsolutePath());
    //bootstrap
    applicationContext = SpringApplication.run(Application.class);
    //await rest api
    await().ignoreExceptions()
      .pollInterval(1, TimeUnit.SECONDS)
      .atMost(60, TimeUnit.SECONDS)
      .untilAsserted(() -> new OkHttpClient.Builder().build()
        .newCall(new Request.Builder()
          .header("Accept","application/json")
          .url("http://localhost:8080/error")
          .get().build())
        .execute());
  }
  @AfterClass
  public static void tearDown(){
    applicationContext.stop();
    config.delete();
    workDir.delete();
  }
}
