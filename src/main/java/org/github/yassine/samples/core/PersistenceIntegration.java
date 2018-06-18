package org.github.yassine.samples.core;

import static org.hibernate.cfg.AvailableSettings.*;

import com.google.common.collect.ImmutableMap;
import com.hazelcast.hibernate.HazelcastLocalCacheRegionFactory;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.github.yassine.samples.domain.repository.CompanyRepository;
import org.hibernate.FlushMode;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.SharedEntityManagerCreator;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(basePackageClasses = {CompanyRepository.class})
public class PersistenceIntegration {

  @Bean
  EntityManager entityManager(EntityManagerFactory emf) {
    return SharedEntityManagerCreator.createSharedEntityManager(emf);
  }

  @Bean
  @Qualifier("transactionManager")
  PlatformTransactionManager platformTransactionManager(EntityManagerFactory entityManagerFactory) {
    JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
    return jpaTransactionManager;
  }

  @Bean
  LocalContainerEntityManagerFactoryBean entityManagerFactory(HikariDataSource dataSource) {
    LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
    emfBean.setDataSource(dataSource);
    emfBean.setPersistenceUnitName("default-pu");
    emfBean.setJpaPropertyMap(getHibernateSettings());
    return emfBean;
  }

  private Map<String, ?> getHibernateSettings() {
    ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();
    builder.put(USE_SQL_COMMENTS, "true");
    builder.put(USE_GET_GENERATED_KEYS, "true");
    builder.put(GENERATE_STATISTICS, "true");
    builder.put(USE_REFLECTION_OPTIMIZER, "true");
    builder.put(ORDER_UPDATES, "true");
    builder.put(ORDER_INSERTS, "true");
    builder.put(SHOW_SQL, "false");
    builder.put(USE_SECOND_LEVEL_CACHE, "true");
    builder.put(USE_QUERY_CACHE, "true");
    builder.put(USE_MINIMAL_PUTS, "true");
    builder.put(CACHE_REGION_FACTORY, HazelcastLocalCacheRegionFactory.class.getName());
    builder.put(FORMAT_SQL, "true");
    builder.put(DIALECT, PostgreSQL9Dialect.class.getName());
    builder.put(DEFAULT_SCHEMA, "public");
    builder.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
    builder.put(PHYSICAL_NAMING_STRATEGY, PlatformDatabaseNamingStrategy.class.getName());
    builder.put("org.hibernate.FlushMode", FlushMode.AUTO.toString());
    builder.put("logValidationErrors", "true");
    return builder.build();
  }

}
