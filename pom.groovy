project {
  modelVersion '4.0.0'
  groupId 'com.github.yassine'
  artifactId 'spring-boot-sample'
  version '0.1.0-SNAPSHOT'
  parent {
    groupId 'org.springframework.boot'
    artifactId 'spring-boot-starter-parent'
    version '2.0.3.RELEASE'
  }
  licenses {
    license {
      name 'The Apache License, Version 2.0'
      url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
    }
  }
  properties{
    'project.build.sourceEncoding' 'UTF-8'
    'sonar.jacoco.reportPaths' '${project.build.directory}/coverage-reports/jacoco-ut.exec'
    'sonar.links.homepage' 'https://github.com/yassine/spring-boot-sample'
    'sonar.links.issue' 'https://github.com/yassine/spring-boot-sample'
    'sonar.links.scm' 'https://github.com/yassine/spring-boot-sample'
    'sonar.projectKey' 'com.github.yassine:spring-boot-sample'
    'sonar.projectName' 'spring-boot-sample'
    'sonar.projectVersion' '${project.version}'
    'sonar.tests' '${project.basedir}/src/test/functional-tests'
    'version.hikari' '2.6.2'
    'version.jersey' '2.22.2'
  }

  dependencies {
    dependency 'com.google.auto.service:auto-service:1.0-rc3'
    dependency 'com.google.guava:guava:24.1-jre'
    dependency 'com.graphql-java:graphql-java-servlet:6.1.3'
    dependency 'io.github.graphql-java:graphql-java-annotations:6.1'
    dependency 'com.hazelcast:hazelcast-hibernate52:1.1.1'
    dependency 'com.hazelcast:hazelcast:3.8.3'
    dependency 'com.zaxxer:HikariCP:3.2.0'
    dependency 'io.airlift:airline:0.8'
    dependency 'io.github.lukehutch:fast-classpath-scanner:2.0.8'
    dependency 'io.reactivex.rxjava2:rxjava:2.1.12'
    dependency 'ma.glasnost.orika:orika-core:1.4.6'
    dependency 'net.jodah:typetools:0.5.0'
    dependency 'org.apache.commons:commons-lang3:3.7'
    dependency 'org.apache.httpcomponents:httpclient:4.5.5'
    dependency 'org.apache.shiro:shiro-web:1.4.0'
    dependency 'org.apache.shiro:shiro-spring:1.4.0'
    dependency 'org.hibernate:hibernate-core:5.2.10.Final'
    dependency 'org.keycloak:keycloak-adapter-core:4.0.0.Final'
    dependency 'org.keycloak:keycloak-core:4.0.0.Final'
    dependency 'org.liquibase:liquibase-core:3.5.3'
    dependency 'org.postgresql:postgresql:9.4.1212.jre7'
    dependency 'org.projectlombok:lombok:1.18.0:provided'
    dependency 'org.springframework.boot:spring-boot-configuration-processor'
    dependency 'org.springframework.boot:spring-boot-starter-data-jpa'
    dependency 'org.springframework.boot:spring-boot-starter-web'
    dependency 'org.springframework:spring-orm:5.0.7.RELEASE'
    dependency{
      groupId 'com.machinezoo.noexception'
      artifactId 'noexception'
      version '1.2.0'
      exclusions{
        exclusion{
          groupId 'org.slf4j'
          artifactId 'slf4j-api'
        }
      }
    }
    //logging
    dependency 'org.slf4j:slf4j-api:1.7.21'
    //test
    dependency 'junit:junit:4.12:test'
    dependency 'org.assertj:assertj-core:3.9.0:test'
    dependency 'org.codehaus.groovy:groovy-all:2.4.13:test'
    dependency 'org.spockframework:spock-core:1.1-groovy-2.4:test'
    dependency 'org.spockframework:spock-guice:1.1-groovy-2.4:test'
    dependency 'org.testcontainers:testcontainers:1.10.6:test'
    dependency 'org.testcontainers:postgresql:1.10.6:test'
    dependency 'org.awaitility:awaitility:3.0.0:test'
    dependency 'commons-io:commons-io:2.6:test'
    dependency 'com.squareup.okhttp3:okhttp:3.10.0:test'
    dependency 'io.rest-assured:rest-assured:3.1.0:test'
    dependency 'org.keycloak:keycloak-admin-client:4.0.0.Final:test'
    dependency 'org.jboss.resteasy:resteasy-client:3.5.1.Final:test'
    dependency 'org.jboss.resteasy:resteasy-jackson2-provider:3.5.1.Final:test'
    dependency 'com.jayway.jsonpath:json-path:2.3.0:test'
    dependency 'org.eclipse:yasson:1.0.1:test'
    dependency 'org.glassfish:javax.json:1.0.4:test'
  }
  build {
    pluginManagement {
      plugins {
        plugin {
          artifactId 'maven-compiler-plugin'
          version '3.7.0'
          configuration {
            source '8'
            target '8'
          }
        }
      }
    }
    plugins {
      plugin {
        groupId 'org.codehaus.mojo'
        artifactId 'build-helper-maven-plugin'
        version '1.9.1'
        executions {
          execution {
            id 'add-source'
            phase 'generate-test-sources'
            goals {
              goal 'add-test-source'
            }
            configuration {
              sources {
                source 'src/test/functional-tests'
                source 'src/test/unit-tests'
              }
            }
          }
        }
      }
      plugin {
        artifactId 'maven-compiler-plugin'
      }
      plugin {
        groupId 'org.jacoco'
        artifactId 'jacoco-maven-plugin'
        version '0.8.3'
        executions {
          execution {
            id 'prepare-agent'
            phase 'test-compile'
            goals {
              goal 'prepare-agent'
            }
            configuration {
              propertyName 'surefireArgLine'
              destFile '${project.build.directory}/coverage-reports/jacoco-ut.exec'
            }
          }
          execution {
            id 'post-test-reports'
            phase 'post-integration-test'
            goals {
              goal 'report'
            }
            configuration {
              dataFile '${project.build.directory}/coverage-reports/jacoco-ut.exec'
              outputDirectory '${project.reporting.outputDirectory}/code-coverage'
            }
          }
        }
      }
      plugin {
        artifactId 'maven-surefire-plugin'
        version '3.0.0-M3'
        executions {
          execution {
            id 'functional-tests'
            goals {
              goal 'test'
            }
            configuration {
              testSourceDirectory '${project.basedir}/src/test/functional-tests'
              testClassesDirectory '${project.build.directory}/test-classes'
            }
          }
        }
        configuration{
          useFile 'false'
          testClassesDirectory '${project.build.directory}/unit-tests'
          testSourceDirectory '${project.basedir}/src/test/unit-tests'
          includes {
            include '**/*Spec'
            include '**/*Test'
          }
          additionalClasspathElements {
            additionalClasspathElement '${project.build.testOutputDirectory}'
            additionalClasspathElement '${project.basedir}/src/test/resources'
          }
          argLine '${surefireArgLine} '//--add-modules java.xml.bind
        }
      }
      plugin {
        groupId 'org.eluder.coveralls'
        artifactId 'coveralls-maven-plugin'
        version '4.3.0'
        configuration {
          repoToken '${env.COVERALLS_REPO_KEY}'
          jacocoReports '${project.reporting.outputDirectory}/code-coverage/jacoco.xml'
        }
      }
      plugin {
        groupId 'org.codehaus.mojo'
        artifactId 'sonar-maven-plugin'
        version '3.6.0.1398'
      }
      plugin {
        groupId 'org.codehaus.mojo'
        artifactId 'properties-maven-plugin'
        version '1.0.0'
        executions {
          execution {
            phase 'initialize'
            goals {
              goal 'read-project-properties'
            }
            configuration {
              quiet 'true'
              files {
                file '${project.basedir}/dev-sonar.properties'
              }
            }
          }
        }
      }
      plugin {
        groupId 'org.apache.maven.plugins'
        artifactId 'maven-shade-plugin'
        configuration {
          'createDependencyReducedPom' true
          keepDependenciesWithProvidedScope true
          filters {
            filter {
              artifact '*:*'
              excludes {
                exclude 'META-INF/*.SF'
                exclude 'META-INF/*.DSA'
                exclude 'META-INF/*.RSA'
              }
            }
          }
        }
        dependencies {
          dependency 'org.springframework.boot:spring-boot-maven-plugin:1.2.7.RELEASE'
        }
        executions {
          execution{
            phase 'package'
            goals {
              goal 'shade'
            }
            configuration {
              transformers('combine.self':'override', 'combine.children':'override') {
                transformer(implementation:'org.apache.maven.plugins.shade.resource.ServicesResourceTransformer') {}
                transformer(implementation:'org.apache.maven.plugins.shade.resource.ManifestResourceTransformer') {
                  mainClass 'org.github.yassine.samples.Application'
                }
                transformer(implementation:'org.apache.maven.plugins.shade.resource.AppendingTransformer') {
                  resource 'META-INF/spring.handlers'
                }
                transformer(implementation:'org.apache.maven.plugins.shade.resource.AppendingTransformer') {
                  resource 'META-INF/spring.schemas'
                }
                transformer(implementation:'org.springframework.boot.maven.PropertiesMergingResourceTransformer') {
                  resource 'META-INF/spring.factories'
                }
              }
            }
          }
        }
      }
    }
  }
  repositories {
    repository {
      id 'jitpack.io'
      url 'https://jitpack.io'
    }
  }
}
