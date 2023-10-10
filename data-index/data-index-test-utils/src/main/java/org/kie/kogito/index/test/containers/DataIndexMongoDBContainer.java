package org.kie.kogito.index.test.containers;

/**
 * This container wraps Data Index Service container
 */
public class DataIndexMongoDBContainer extends AbstractDataIndexContainer {

    public static final String NAME = "data-index-service-mongodb";

    public DataIndexMongoDBContainer() {
        super(NAME);
    }

    public void setMongoDBURL(String mongoURL) {
        addEnv("QUARKUS_MONGODB_CONNECTION_STRING", mongoURL);
    }

    @Override
    public String getResourceName() {
        return NAME;
    }

}
