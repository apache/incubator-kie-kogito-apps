package org.kie.kogito.jobs.service.utils;

import java.util.function.Function;

@FunctionalInterface
public interface FunctionsUtil<T, R, E extends Throwable> {

    R apply(T t) throws E;

    @SuppressWarnings({ "squid:S1181", "squid:S00112" })
    static <T, R, E extends Throwable> Function<T, R> unchecked(FunctionsUtil<T, R, E> f) {
        return t -> {
            try {
                return f.apply(t);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        };
    }
}