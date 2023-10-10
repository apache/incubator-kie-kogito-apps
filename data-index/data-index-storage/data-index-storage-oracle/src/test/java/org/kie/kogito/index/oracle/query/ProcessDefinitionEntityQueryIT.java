package org.kie.kogito.index.oracle.query;

import javax.inject.Inject;

import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.oracle.storage.ProcessDefinitionEntityStorage;
import org.kie.kogito.index.test.query.AbstractProcessDefinitionQueryIT;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.testcontainers.quarkus.OracleSqlQuarkusTestResource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(OracleSqlQuarkusTestResource.class)
class ProcessDefinitionEntityQueryIT extends AbstractProcessDefinitionQueryIT {

    @Inject
    ProcessDefinitionEntityStorage storage;

    @Override
    public Storage<String, ProcessDefinition> getStorage() {
        return storage;
    }

}
