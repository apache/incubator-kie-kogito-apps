/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.inmemory;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.index.AbstractProcessDataIndexIT;
import org.kie.kogito.index.quarkus.DataIndexInMemoryQuarkusTestResource;
import org.kie.kogito.index.quarkus.InMemoryTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(InMemoryTestProfile.class)
public class ProcessDataIndexInMemoryIT extends AbstractProcessDataIndexIT {

    @ConfigProperty(name = DataIndexInMemoryQuarkusTestResource.KOGITO_DATA_INDEX_SERVICE_URL)
    String dataIndex;

    @Override
    public String getDataIndexURL() {
        return dataIndex;
    }

    @Override
    public boolean validateDomainData() {
        return false;
    }
}
