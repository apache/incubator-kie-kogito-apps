package org.kie.kogito.index.test.query;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;
import org.kie.kogito.index.model.ProcessDefinition;
import org.kie.kogito.index.test.QueryTestBase;
import org.kie.kogito.index.test.TestUtils;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.SortDirection;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.contains;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.containsAll;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.containsAny;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.equalTo;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.in;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.isNull;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.notNull;
import static org.kie.kogito.persistence.api.query.QueryFilterFactory.orderBy;

public abstract class AbstractProcessDefinitionQueryIT extends QueryTestBase<String, ProcessDefinition> {

    @Test
    void testProcessDefinitionQuery() {
        String processId = "travels";
        ProcessDefinition pdv1 = TestUtils.createProcessDefinition(processId, "1.0", Set.of("admin", "kogito"));
        Storage<String, ProcessDefinition> storage = getStorage();
        storage.put(pdv1.getKey(), pdv1);
        ProcessDefinition pdv2 = TestUtils.createProcessDefinition(processId, "2.0", Set.of("kogito"));
        storage.put(pdv2.getKey(), pdv2);

        queryAndAssert(assertWithKey(), storage, singletonList(isNull("type")), null, null, null, pdv1.getKey(), pdv2.getKey());
        queryAndAssert(assertWithKey(), storage, singletonList(notNull("version")), null, null, null, pdv1.getKey(), pdv2.getKey());
        queryAndAssert(assertWithKey(), storage, singletonList(equalTo("version", pdv1.getVersion())), null, null, null, pdv1.getKey());
        queryAndAssert(assertWithKey(), storage, singletonList(contains("roles", "admin")), null, null, null, pdv1.getKey());
        queryAndAssert(assertWithKey(), storage, singletonList(containsAny("roles", asList("admin", "kogito"))), null, null, null, pdv1.getKey(), pdv2.getKey());
        queryAndAssert(assertWithKey(), storage, singletonList(containsAll("roles", asList("admin", "kogito"))), null, null, null, pdv1.getKey());
        queryAndAssert(assertWithKey(), storage, asList(in("id", asList(pdv1.getId(), pdv2.getId())),
                in("version", asList(pdv1.getVersion(), pdv2.getVersion()))),
                singletonList(orderBy("version", SortDirection.ASC)), 1, 1, pdv2.getKey());
        queryAndAssert(assertWithKey(), storage, null, singletonList(orderBy("version", SortDirection.DESC)), null,
                null, pdv2.getKey(), pdv1.getKey());
    }

    public static <V> BiConsumer<List<V>, String[]> assertWithKey() {
        return (instances, ids) -> assertThat(instances).hasSize(ids == null ? 0 : ids.length).extracting("key").containsExactly(ids);
    }

}
