package org.kie.kogito.app.audit.quarkus;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class QuarkusDataAuditMain {

    public static void main(String... args) {
        Quarkus.run(args);
    }
}