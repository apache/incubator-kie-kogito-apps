package org.kie.kogito.test.resources;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.quarkus.test.common.QuarkusTestResource;

@QuarkusTestResource(JobServiceCompositeQuarkusTestResource.class)
@QuarkusTestResource(KogitoServiceRandomPortQuarkusTestResource.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JobServiceTestResource {

    enum Persistence {
        POSTGRESQL,
        IN_MEMORY;
    }

    boolean kafkaEnabled() default false;

    Persistence persistence() default Persistence.POSTGRESQL;

    boolean knativeEventingEnabled() default false;

    boolean dataIndexEnabled() default false;
}