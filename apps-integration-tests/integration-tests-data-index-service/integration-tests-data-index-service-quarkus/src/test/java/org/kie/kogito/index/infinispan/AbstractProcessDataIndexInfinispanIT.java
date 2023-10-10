package org.kie.kogito.index.infinispan;

import org.kie.kogito.index.AbstractProcessDataIndexIT;
import org.kie.kogito.test.quarkus.QuarkusTestProperty;

import static org.kie.kogito.index.test.Constants.KOGITO_DATA_INDEX_SERVICE_URL;

public abstract class AbstractProcessDataIndexInfinispanIT extends AbstractProcessDataIndexIT {

    @QuarkusTestProperty(name = KOGITO_DATA_INDEX_SERVICE_URL)
    String dataIndex;

    @Override
    public String getDataIndexURL() {
        return dataIndex;
    }

    @Override
    public boolean validateGetProcessInstanceSource() {
        return true;
    }
}
