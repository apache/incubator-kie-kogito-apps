package org.kie.kogito.index.quarkus.http;

import java.util.Arrays;
import java.util.List;

import org.kie.kogito.index.test.quarkus.http.DataIndexPostgreSqlHttpQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class PostgreSqlHttpTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(KogitoServiceRandomPortQuarkusHttpTestResource.class),
                new TestResourceEntry(DataIndexPostgreSqlHttpQuarkusTestResource.class));
    }

}
