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

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
public class SpringbootAuditDataConfiguration {

    // TODO Jackson 3 migration: Spring Boot 4 only auto-configures a Jackson 3 ObjectMapper
    // (tools.jackson.databind.ObjectMapper). The data-audit Spring controller and tests still use
    // Jackson 2 (com.fasterxml.jackson.databind), so we register a Jackson 2 ObjectMapper as a
    // transition shim. @ConditionalOnMissingBean defers to any consumer-provided one (e.g. a
    // kogito-codegen-generated GlobalObjectMapper). Remove this bean once the addon migrates to
    // Jackson 3 (mirrors the kogito-runtimes GlobalObjectMapperSpringTemplate shim).
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }

    // TODO Jackson 3 migration: Spring Boot 4 only auto-registers a Jackson 3 HTTP message converter,
    // but GraphQLAuditDataRouteMapping uses @RequestBody com.fasterxml.jackson.databind.JsonNode
    // (Jackson 2). Without a Jackson 2 converter Spring MVC returns 400 Bad Request for the
    // /audit/q POST. Removing in lock-step with the Jackson 2 ObjectMapper bean above once the
    // controller migrates to Jackson 3.
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
        return new MappingJackson2HttpMessageConverter(objectMapper);
    }

    // Hibernate 7 + Spring ORM 6.2 workaround: Hibernate 7's SessionFactory.getSchemaManager()
    // returns org.hibernate.relational.SchemaManager, conflicting with JPA 3.2's
    // EntityManagerFactory.getSchemaManager() returning jakarta.persistence.SchemaManager.
    // Force plain JPA interface to avoid JDK Proxy incompatible return type error.
    @Bean
    public static BeanPostProcessor auditDataEmfPostProcessor() {
        return new BeanPostProcessor() {
            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) {
                if (bean instanceof LocalContainerEntityManagerFactoryBean emfb) {
                    emfb.setEntityManagerFactoryInterface(jakarta.persistence.EntityManagerFactory.class);
                }
                return bean;
            }
        };
    }
}
