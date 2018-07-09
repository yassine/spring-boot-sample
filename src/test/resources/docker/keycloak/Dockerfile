FROM alpine:edge

ENV KEYCLOAK_VERSION 4.0.0.Final
ENV KEYCLOAK_USER dev
ENV KEYCLOAK_PASSWORD dev
ENV KEYCLOAK_HOME /opt/jboss/keycloak

COPY transform.xsl /tmp/transform.xsl

RUN apk update && apk upgrade && \
    apk add --no-cache -u openjdk8-jre-base unzip tar ca-certificates curl && \
    curl -L https://downloads.sourceforge.net/project/saxon/Saxon-HE/9.8/SaxonHE9-8-0-3J.zip -o /tmp/saxon.zip && \
    cd /tmp && \
    unzip saxon.zip -d /tmp/saxon && \
    mkdir -p /opt/jboss && \
    wget https://downloads.jboss.org/keycloak/$KEYCLOAK_VERSION/keycloak-$KEYCLOAK_VERSION.tar.gz -O /tmp/keycloak.tar.gz && \
    cd /tmp && \
    tar -zxf /tmp/keycloak.tar.gz && \
    cd /tmp && mv keycloak-$KEYCLOAK_VERSION $KEYCLOAK_HOME && \
    java -jar /tmp/saxon/saxon9he.jar -xsl:/tmp/transform.xsl -s:$KEYCLOAK_HOME/standalone/configuration/standalone.xml -o:$KEYCLOAK_HOME/standalone/configuration/standalone.xml && \
    mkdir -p $KEYCLOAK_HOME/modules/system/layers/base/org/postgresql/jdbc/main && \
    cd $KEYCLOAK_HOME/modules/system/layers/base/org/postgresql/jdbc/main && \
    curl -L http://central.maven.org/maven2/org/postgresql/postgresql/9.4.1212.jre7/postgresql-9.4.1212.jre7.jar -o ./postgresql-9.4.1212.jre7.jar && \
    $KEYCLOAK_HOME/bin/add-user-keycloak.sh -r master --user $KEYCLOAK_USER --password $KEYCLOAK_PASSWORD && \
    $KEYCLOAK_HOME/bin/add-user-keycloak.sh -r master --user admin --password admin && \
    rm -rf /tmp/saxon.zip /tmp/keycloak.tar.gz /tmp/saxon/ /tmp/transform.xsl && \
    apk del ca-certificates curl unzip tar

COPY module.xml /opt/jboss/keycloak/modules/system/layers/base/org/postgresql/jdbc/main/module.xml
COPY import-realm.json /opt/jboss/keycloak/import-realm.json

EXPOSE 8080 

ENTRYPOINT ["/opt/jboss/keycloak/bin/standalone.sh"]
CMD ["-b", "0.0.0.0", "-Dkeycloak.import=/opt/jboss/keycloak/import-realm.json"]
