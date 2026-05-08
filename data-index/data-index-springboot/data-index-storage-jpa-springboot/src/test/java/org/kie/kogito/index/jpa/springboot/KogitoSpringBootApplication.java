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
package org.kie.kogito.index.jpa.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication(scanBasePackages = { "org.kie.kogito.**" })
public class KogitoSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(KogitoSpringBootApplication.class, args);
    }

    // TODO Jackson 3 migration: Spring Boot 4 only auto-configures Jackson 3 (tools.jackson.databind);
    // this test fixture bridges to the Jackson 2 ObjectMapper that UserTaskInstanceEntityMapperIT
    // (and the data-index JPA mappers) inject. Remove once the data-index Spring side migrates
    // to Jackson 3 (mirrors the kogito-runtimes GlobalObjectMapperSpringTemplate shim).
    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json().build();
    }
}
