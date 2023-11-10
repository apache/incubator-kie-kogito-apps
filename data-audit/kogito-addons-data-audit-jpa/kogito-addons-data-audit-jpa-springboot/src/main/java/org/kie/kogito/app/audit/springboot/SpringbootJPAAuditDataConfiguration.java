/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.kie.kogito.app.audit.springboot;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration(proxyBeanMethods = false)
@PropertySource("classpath:/application-data-audit.properties")
public class SpringbootJPAAuditDataConfiguration {

    @Bean
    @Primary
    public JpaProperties primaryJpaDataAuditProperties() {
        return new JpaProperties();
    }

    @Bean(name = "JPADataAuditProperties")
    @Qualifier("JPADataAuditProperties")
    @ConfigurationProperties(prefix = "data-audit.spring.jpa")
    public JpaProperties jpaDataAuditProperties() {
        return new JpaProperties();
    }

    @Bean(name = "jpaDataAuditEntityManagerFactory")
    @Qualifier("jpaDataAuditEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean jpaDataAuditEntityManagerFactory(
            DataSource dataSource,
            @Autowired @Qualifier("JPADataAuditProperties") JpaProperties jpaProperties) {
        EntityManagerFactoryBuilder builder = createEntityManagerFactoryBuilder(jpaProperties);
        return builder
                .dataSource(dataSource)
                .mappingResources(jpaProperties.getMappingResources().toArray(String[]::new))
                .properties(jpaProperties.getProperties())
                .persistenceUnit("DataAuditPU")
                .build();
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter jpaVendorAdapter = createJpaVendorAdapter(jpaProperties);
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
    }

    private JpaVendorAdapter createJpaVendorAdapter(JpaProperties jpaProperties) {
        HibernateJpaVendorAdapter hibernate = new HibernateJpaVendorAdapter();
        hibernate.setDatabase(jpaProperties.getDatabase());
        hibernate.setGenerateDdl(jpaProperties.isGenerateDdl());
        hibernate.setShowSql(jpaProperties.isShowSql());
        hibernate.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        return hibernate;
    }
}
