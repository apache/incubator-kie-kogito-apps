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
package org.kie.kogito.index.graphql.query;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.index.json.JsonUtils.jsonFilter;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.*;

public class GraphQLQueryMapperTest {

    private GraphQLQueryMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new GraphQLQueryMapper();
    }

    @Test
    void testJsonMapperEqual() {
        assertThat(mapper.mapJsonArgument("variables").apply(Map.of("workflowdata", Map.of("number", Map.of("equal", 1))))).containsExactly(
                jsonFilter(equalTo("variables.workflowdata.number", 1)));
    }

    @Test
    void testJsonMapperGreater() {
        assertThat(mapper.mapJsonArgument("variables").apply(Map.of("workflowdata", Map.of("number", Map.of("greaterThan", 1))))).containsExactly(
                jsonFilter(greaterThan("variables.workflowdata.number", 1)));
    }

    @Test
    void testJsonMapperLess() {
        assertThat(mapper.mapJsonArgument("variables").apply(Map.of("workflowdata", Map.of("number", Map.of("lessThan", 1))))).containsExactly(
                jsonFilter(lessThan("variables.workflowdata.number", 1)));
    }

    @Test
    void testJsonMapperGreaterEqual() {
        assertThat(mapper.mapJsonArgument("variables").apply(Map.of("workflowdata", Map.of("number", Map.of("greaterThanEqual", 1))))).containsExactly(
                jsonFilter(greaterThanEqual("variables.workflowdata.number", 1)));
    }

    @Test
    void testJsonMapperLessEqual() {
        assertThat(mapper.mapJsonArgument("variables").apply(Map.of("workflowdata", Map.of("number", Map.of("lessThanEqual", 1))))).containsExactly(
                jsonFilter(lessThanEqual("variables.workflowdata.number", 1)));
    }

    @Test
    void testJsonMapperBetween() {
        assertThat(mapper.mapJsonArgument("variables").apply(Map.of("workflowdata", Map.of("number", Map.of("between", Map.of("from", 1, "to", 3)))))).containsExactly(
                jsonFilter(between("variables.workflowdata.number", 1, 3)));
    }
}
