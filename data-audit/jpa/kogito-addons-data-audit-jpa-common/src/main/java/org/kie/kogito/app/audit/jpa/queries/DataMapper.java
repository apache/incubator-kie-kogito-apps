package org.kie.kogito.app.audit.jpa.queries;

import java.util.List;

public interface DataMapper<R> {
    List<R> produce(List<Object[]> data);
}