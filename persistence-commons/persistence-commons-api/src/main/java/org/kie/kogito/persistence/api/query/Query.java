package org.kie.kogito.persistence.api.query;

import java.util.List;

public interface Query<T> {

    Query<T> limit(Integer limit);

    Query<T> offset(Integer offset);

    Query<T> filter(List<AttributeFilter<?>> filters);

    Query<T> sort(List<AttributeSort> sortBy);

    List<T> execute();
}
