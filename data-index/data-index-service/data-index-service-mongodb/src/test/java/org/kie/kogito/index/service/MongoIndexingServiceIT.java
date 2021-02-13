/*
 * Copyright 2020 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.kogito.index.service;

import org.kie.kogito.index.TestUtils;
import org.kie.kogito.testcontainers.quarkus.MongoDBQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(MongoDBQuarkusTestResource.class)
class MongoIndexingServiceIT extends AbstractIndexingServiceIT {

    @Override
    protected String getProcessProtobufFileContent() throws Exception {
        return TestUtils.readFileContent("travels-mongo.proto");
    }

    @Override
    protected String getUserTaskProtobufFileContent() throws Exception {
        return TestUtils.readFileContent("deals-mongo.proto");
    }
}
