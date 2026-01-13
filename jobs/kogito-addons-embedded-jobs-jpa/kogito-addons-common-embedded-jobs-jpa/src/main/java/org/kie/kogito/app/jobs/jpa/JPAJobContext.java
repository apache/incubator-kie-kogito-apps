package org.kie.kogito.app.jobs.jpa;

import org.kie.kogito.app.jobs.spi.JobContext;

import jakarta.persistence.EntityManager;

public interface JPAJobContext extends JobContext {
    EntityManager getEntityManager();
}
