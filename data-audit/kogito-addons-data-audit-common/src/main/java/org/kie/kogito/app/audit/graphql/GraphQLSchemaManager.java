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
package org.kie.kogito.app.audit.graphql;

import java.io.IOException;
import java.io.InputStream;
import java.util.ServiceLoader;

import org.kie.kogito.app.audit.spi.GraphQLSchemaQuery;
import org.kie.kogito.app.audit.spi.GraphQLSchemaQueryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

import static graphql.schema.idl.RuntimeWiring.newRuntimeWiring;

public class GraphQLSchemaManager {

    private static final GraphQLSchemaManager INSTANCE = new GraphQLSchemaManager();

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLSchemaManager.class);

    private GraphQLSchema graphQLSchema;

    public static GraphQLSchemaManager graphQLSchemaManagerInstance() {
        return INSTANCE;
    }

    private GraphQLSchemaManager() {
        try (InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("META-INF/data-audit.graphqls")) {
            SchemaParser schemaParser = new SchemaParser();
            TypeDefinitionRegistry typeDefinitionRegistry = schemaParser.parse(is);

            RuntimeWiring.Builder runtimeWiringBuilder = newRuntimeWiring();

            runtimeWiringBuilder.scalar(ExtendedScalars.GraphQLBigInteger);
            runtimeWiringBuilder.scalar(ExtendedScalars.GraphQLLong);
            runtimeWiringBuilder.scalar(ExtendedScalars.Date);
            runtimeWiringBuilder.scalar(ExtendedScalars.DateTime);
            runtimeWiringBuilder.scalar(ExtendedScalars.Json);

            ServiceLoader.load(GraphQLSchemaQueryProvider.class).forEach(queryProvider -> {
                for (GraphQLSchemaQuery<?> query : queryProvider.queries()) {
                    runtimeWiringBuilder.type("Query", builder -> builder.dataFetcher(query.name(), query::fetch));
                }
            });

            RuntimeWiring runtimeWiring = runtimeWiringBuilder.build();

            SchemaGenerator schemaGenerator = new SchemaGenerator();
            graphQLSchema = schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        } catch (IOException e) {
            LOGGER.error("could not find data-audit.graphqls", e);
        }
    }

    public GraphQLSchema getGraphQLSchema() {
        return graphQLSchema;
    }

}
