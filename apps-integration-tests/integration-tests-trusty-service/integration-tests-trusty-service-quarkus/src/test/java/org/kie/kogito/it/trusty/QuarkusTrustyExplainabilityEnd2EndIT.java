package org.kie.kogito.it.trusty;

import org.kie.kogito.testcontainers.QuarkusKogitoServiceContainer;

public class QuarkusTrustyExplainabilityEnd2EndIT extends AbstractTrustyExplainabilityEnd2EndIT {

    public QuarkusTrustyExplainabilityEnd2EndIT() {
        super(QuarkusKogitoServiceContainer::new);
    }
}
