package org.kie.kogito.index.mongodb;

import org.kie.kogito.index.quarkus.http.MongoDBHttpTestProfile;

import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.quarkus.test.junit.TestProfile;

@QuarkusIntegrationTest
@TestProfile(MongoDBHttpTestProfile.class)
public class ProcessDataIndexMongoDBHttpIT extends AbstractProcessDataIndexMongoDBIT {
}
