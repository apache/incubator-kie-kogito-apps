package org.kie.kogito.index.test.containers;

/**
 * This container wraps Data Index Service container
 */
public class DataIndexInfinispanContainer extends AbstractDataIndexContainer {

    public static final String NAME = "data-index-service-infinispan";

    public DataIndexInfinispanContainer() {
        super(NAME);
    }

    public void setInfinispanURL(String infinispanURL) {
        addEnv("QUARKUS_INFINISPAN_CLIENT_HOSTS", infinispanURL);
        addEnv("QUARKUS_INFINISPAN_CLIENT_USE_AUTH", "false");
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

}
