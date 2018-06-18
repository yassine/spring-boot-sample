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
  properties{
    'project.build.sourceEncoding' 'UTF-8'
    'sonar.jacoco.reportPaths' '${project.build.directory}/coverage-reports/jacoco-ut.exec'
    'sonar.links.homepage' 'https://github.com/yassine/spring-boot-sample'
    'sonar.links.issue' 'https://github.com/yassine/spring-boot-sample'
    'sonar.links.scm' 'https://github.com/yassine/spring-boot-sample'
    'sonar.projectKey' 'com.github.yassine:spring-boot-sample'
    'sonar.projectName' 'spring-boot-sample'
    'sonar.projectVersion' '${project.version}'
    'sonar.tests' '${project.basedir}/src/test/unit-tests,${project.basedir}/src/test/functional-tests'
    'version.hikari' '2.6.2'
    'version.jersey' '2.22.2'
  }

  dependencies {
    dependency 'com.google.auto.service:auto-service:1.0-rc3'
    dependency 'com.google.guava:guava:24.1-jre'
    dependency 'com.hazelcast:hazelcast-hibernate52:1.1.1'
    dependency 'com.hazelcast:hazelcast:3.8.3'
    dependency 'com.zaxxer:HikariCP:3.2.0'
    dependency 'io.airlift:airline:0.8'
    dependency 'io.github.lukehutch:fast-classpath-scanner:2.0.8'
    dependency 'io.reactivex.rxjava2:rxjava:2.1.12'
    dependency 'ma.glasnost.orika:orika-core:1.4.6'
    dependency 'net.jodah:typetools:0.5.0'
    dependency 'org.hibernate:hibernate-core:5.2.10.Final'
    dependency 'org.liquibase:liquibase-core:3.5.3'
    dependency 'org.postgresql:postgresql:9.4.1212.jre7'
    dependency 'org.projectlombok:lombok:1.18.0:provided'
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
    dependency 'org.testcontainers:testcontainers:1.8.0:test'
    dependency 'org.testcontainers:postgresql:1.8.0:test'
    dependency 'org.awaitility:awaitility:3.0.0:test'
    dependency 'commons-io:commons-io:2.6:test'
    dependency 'com.squareup.okhttp3:okhttp:3.10.0:test'
    dependency 'io.rest-assured:rest-assured:3.1.0:test'

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
        artifactId 'maven-compiler-plugin'
      }
      plugin {
        groupId 'org.jacoco'
        artifactId 'jacoco-maven-plugin'
        version '0.8.0'
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
        groupId 'org.codehaus.gmavenplus'
        artifactId 'gmavenplus-plugin'
        version '1.6'
        executions {
          execution {
            id 'generate-unit-tests'
            goals {
              goal 'compileTests'
              goal 'addTestSources'
            }
            configuration {
              testSources {
                testSource {
                  directory '${project.basedir}/src/test/unit-tests'
                  includes {
                    include '**/*.groovy'
                  }
                }
              }
              outputDirectory '${project.build.directory}/unit-tests'
            }
          }
          execution {
            id 'generate-functional-tests'
            goals {
              goal 'compileTests'
              goal 'addTestSources'
            }
            configuration {
              testSources {
                testSource {
                  directory '${project.basedir}/src/test/functional-tests'
                  includes {
                    include '**/*.groovy'
                  }
                }
              }
              outputDirectory '${project.build.directory}/functional-tests'
            }
          }
        }
      }
      plugin {
        artifactId 'maven-surefire-plugin'
        version '2.20.1'
        executions {
          execution {
            id 'functional-tests'
            goals {
              goal 'test'
            }
            configuration {
              testClassesDirectory '${project.build.directory}/functional-tests'
              testSourceDirectory '{project.basedir}/src/test/functional-tests'
            }
          }
          execution {
            id 'default-tests'
            goals {
              goal 'test'
            }
            configuration {
              testClassesDirectory '${project.build.directory}/test-classes'
            }
          }
        }
        configuration{
          useFile 'false'
          testClassesDirectory '${project.build.directory}/unit-tests'
          testSourceDirectory '{project.basedir}/src/test/unit-tests'
          includes {
            include '**/*Spec'
            include '**/*Test'
          }
          additionalClasspathElements {
            additionalClasspathElement '${project.build.testOutputDirectory}'
            additionalClasspathElement '${project.basedir}/src/test/resources'
          }
          argLine '${surefireArgLine}'
          properties {
            'listener' 'org.github.yassine.samples.TestRunListener'
          }
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
        version '3.4.0.905'
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
    }
  }
  repositories {
    repository {
      id 'jitpack.io'
      url 'https://jitpack.io'
    }
  }
}
