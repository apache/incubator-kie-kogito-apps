package org.kie.kogito.index.quarkus;

import java.util.Arrays;
import java.util.List;

import org.kie.kogito.index.quarkus.http.KogitoServiceRandomPortQuarkusHttpTestResource;
import org.kie.kogito.index.test.quarkus.DataIndexInMemoryQuarkusTestResource;

import io.quarkus.test.junit.QuarkusTestProfile;

public class InMemoryTestProfile implements QuarkusTestProfile {

    @Override
    public List<TestResourceEntry> testResources() {
        return Arrays.asList(new TestResourceEntry(KogitoServiceRandomPortQuarkusHttpTestResource.class),
                new TestResourceEntry(DataIndexInMemoryQuarkusTestResource.class));
    }

}
