package org.kie.kogito.it.trusty;

import org.junit.jupiter.api.Disabled;
import org.kie.kogito.testcontainers.QuarkusKogitoServiceContainer;

import io.quarkus.test.junit.NativeImageTest;

@NativeImageTest
@Disabled("https://issues.redhat.com/browse/FAI-748")
public class NativeQuarkusTrustyExplainabilityEnd2EndIT extends AbstractTrustyExplainabilityEnd2EndIT {

    public NativeQuarkusTrustyExplainabilityEnd2EndIT() {
        super(QuarkusKogitoServiceContainer::new);
    }
}
