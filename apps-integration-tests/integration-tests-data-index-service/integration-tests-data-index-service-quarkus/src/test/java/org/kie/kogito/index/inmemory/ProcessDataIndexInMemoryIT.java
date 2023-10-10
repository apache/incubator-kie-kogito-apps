package org.kie.kogito.index.inmemory;

import org.kie.kogito.index.AbstractProcessDataIndexIT;
import org.kie.kogito.index.quarkus.InMemoryTestProfile;
import org.kie.kogito.test.quarkus.QuarkusTestProperty;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

@QuarkusIntegrationTest
@TestProfile(InMemoryTestProfile.class)
public class ProcessDataIndexInMemoryIT extends AbstractProcessDataIndexIT {

    @QuarkusTestProperty(name = KOGITO_DATA_INDEX_SERVICE_URL)
    String dataIndex;

    @Override
    public String getDataIndexURL() {
        return dataIndex;
    }

    @Override
    public boolean validateDomainData() {
        return false;
    }

    @Override
    public boolean validateGetProcessInstanceSource() {
        return true;
    }
}
