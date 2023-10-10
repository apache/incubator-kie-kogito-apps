package org.kie.kogito.jobs.service.utils;

import java.util.function.Function;

import org.eclipse.microprofile.reactive.streams.operators.PublisherBuilder;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandling {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandling.class);

    private ErrorHandling() {

    }

    /**
     * Utility method that receives execute function that returns a {@link Publisher} and skip any the error element,
     * returning an empty item. It can be used while processing Reactive Streams when it is necessary to continue the
     * processing even with an error on some operation on the Stream.
     *
     *
     * @param function Function to be executed
     * @param input Input object
     * @param <R> return type
     * @param <T> input type
     * @return
     */
    public static <R, T> Publisher<R> skipErrorPublisher(Function<? super T, Publisher<R>> function, T input) {
        return ReactiveStreams
                .fromPublisher(function.apply(input))
                .onError(t -> LOGGER.warn("Error skipped when processing {}.", input, t))
                .onErrorResumeWithRsPublisher(t -> ReactiveStreams.<R> empty().buildRs())
                .buildRs();
    }

    /**
     * Utility method that receives execute function that returns a {@link Publisher} and skip any the error element,
     * returning an empty item. It can be used while processing Reactive Streams when it is necessary to continue the
     * processing even with an error on some operation on the Stream.
     *
     *
     * @param function Function to be executed
     * @param input Input object
     * @param <R> return type
     * @param <T> input type
     * @return
     */
    public static <R, T> PublisherBuilder<R> skipErrorPublisherBuilder(Function<? super T, PublisherBuilder<R>> function, T input) {
        return function.apply(input)
                .onError(t -> LOGGER.warn("Error skipped when processing {}.", input, t))
                .onErrorResumeWithRsPublisher(t -> ReactiveStreams.<R> empty().buildRs());
    }
}
