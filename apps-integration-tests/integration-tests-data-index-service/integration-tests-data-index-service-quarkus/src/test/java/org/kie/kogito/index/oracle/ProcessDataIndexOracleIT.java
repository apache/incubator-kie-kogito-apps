/*
 * Copyright 2022 Red Hat, Inc. and/or its affiliates.
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
package org.kie.kogito.index.oracle;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.kogito.index.AbstractProcessDataIndexIT;
import org.kie.kogito.index.quarkus.DataIndexOracleQuarkusTestResource;
import org.kie.kogito.index.quarkus.OracleTestProfile;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusTest
@TestProfile(OracleTestProfile.class)
public class ProcessDataIndexOracleIT extends AbstractProcessDataIndexIT {

    @ConfigProperty(name = DataIndexOracleQuarkusTestResource.KOGITO_DATA_INDEX_SERVICE_URL)
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