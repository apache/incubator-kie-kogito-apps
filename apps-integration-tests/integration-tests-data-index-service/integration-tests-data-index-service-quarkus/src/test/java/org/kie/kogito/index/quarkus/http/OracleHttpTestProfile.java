package org.kie.kogito.index.quarkus.http;

import java.util.Arrays;
import java.util.List;

import org.kie.kogito.index.test.quarkus.http.DataIndexOracleHttpQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class OracleHttpTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(KogitoServiceRandomPortQuarkusHttpTestResource.class),
                new TestResourceEntry(DataIndexOracleHttpQuarkusTestResource.class));
    }

}
