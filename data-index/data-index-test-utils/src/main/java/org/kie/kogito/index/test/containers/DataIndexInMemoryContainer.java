package org.kie.kogito.index.test.containers;

/**
 * This container wraps Data Index Service container
 */
public class DataIndexInMemoryContainer extends AbstractDataIndexContainer {

    public static final String NAME = "data-index-service-inmemory";

    public DataIndexInMemoryContainer() {
        super(NAME);
        withPrivilegedMode(true);
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

}
