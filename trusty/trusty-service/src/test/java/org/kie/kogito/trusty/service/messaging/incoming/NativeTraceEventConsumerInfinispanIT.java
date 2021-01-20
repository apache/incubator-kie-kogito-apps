package org.kie.kogito.trusty.service.messaging.incoming;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
public class NativeTraceEventConsumerInfinispanIT extends TraceEventConsumerInfinispanIT {
    // Execute the same tests but in native mode.
}
