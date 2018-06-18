package org.github.yassine.samples

import com.github.dockerjava.api.model.PortBinding
import com.google.common.io.Files
import lombok.extern.slf4j.Slf4j
import okhttp3.OkHttpClient
import okhttp3.Request

import org.apache.commons.io.IOUtils
import org.junit.ClassRule
import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.GenericContainer
import spock.lang.Shared
import spock.lang.Specification
import org.testcontainers.containers.PostgreSQLContainer

import java.nio.charset.Charset
import java.sql.DriverManager
import java.util.concurrent.TimeUnit
import java.util.UUID

import static org.awaitility.Awaitility.await
import static io.restassured.RestAssured.*
import static io.restassured.matcher.RestAssuredMatchers.*
import static org.hamcrest.Matchers.*

@Slf4j
class RestApiSpec extends Specification {

  @ClassRule @Shared
  GenericContainer postgres = new PostgreSQLContainer("postgres:9.6-alpine")
                                    .withUsername("dev")
                                    .withPassword("dev")
                                    .withDatabaseName("spring_boot_sample_db")
                                    .withExposedPorts(5432)
                                    .withCreateContainerCmdModifier({
                                      cmd -> cmd.withPortBindings(PortBinding.parse("5432:5432"))
                                    })
  @Shared
  ConfigurableApplicationContext applicationContext;
  @Shared
  File workDir = Files.createTempDir()
  @Shared
  File config  = new File(workDir, "application-test.properties")

  def setupSpec() {
    IOUtils.copy(getClass().getResourceAsStream("/application-test.properties"), new FileOutputStream(config))
    Properties props = new Properties()
    props.load(getClass().getResourceAsStream("/application-test.properties"))

    props.stringPropertyNames()
      .forEach({name -> System.setProperty(name, props.getProperty(name))})

    //await db
    await().ignoreExceptions().pollInterval(1, TimeUnit.SECONDS).atMost(20, TimeUnit.SECONDS)
        .untilAsserted({ ->
          DriverManager.getConnection(props.getProperty("spring.datasource.url"),"dev","dev")
        })
    //migrate
    Application.main("db", "install", "-c", config.getAbsolutePath())
    Application.main("db", "init-data", "-c", config.getAbsolutePath())
    //bootstrap
    applicationContext = SpringApplication.run(Application.class)
    //await rest api
    await().ignoreExceptions()
      .pollInterval(1, TimeUnit.SECONDS)
      .atMost(60, TimeUnit.SECONDS)
      .untilAsserted({ ->
        new OkHttpClient.Builder().build()
          .newCall(new Request.Builder()
          .header("Accept","application/json")
          .url(String.format("http://localhost:8080/error"))
          .get().build())
          .execute()
      })
  }

  def cleanupSpec() {
    applicationContext.stop()
    config.delete()
    workDir.deleteDir()
  }

  def "Main"() {
    given:
      String id = given()
          .body(IOUtils.toString(getClass().getResourceAsStream("/fixtures/create-company.json"), Charset.defaultCharset()))
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
          .path("id")

      UUID uuid = UUID.fromString(id)

    expect:
      true
  }

}
