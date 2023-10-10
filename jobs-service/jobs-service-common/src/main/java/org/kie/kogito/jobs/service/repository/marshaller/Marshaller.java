package org.kie.kogito.jobs.service.repository.marshaller;

public interface Marshaller<T, R> {

    R marshall(T value);

    T unmarshall(R value);
}
