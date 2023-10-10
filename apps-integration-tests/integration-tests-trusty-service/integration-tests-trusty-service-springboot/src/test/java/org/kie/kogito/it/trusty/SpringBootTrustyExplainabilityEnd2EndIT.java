package org.kie.kogito.it.trusty;

import org.kie.kogito.testcontainers.SpringBootKogitoServiceContainer;

public class SpringBootTrustyExplainabilityEnd2EndIT extends AbstractTrustyExplainabilityEnd2EndIT {

    public SpringBootTrustyExplainabilityEnd2EndIT() {
        super(SpringBootKogitoServiceContainer::new);
    }
}
