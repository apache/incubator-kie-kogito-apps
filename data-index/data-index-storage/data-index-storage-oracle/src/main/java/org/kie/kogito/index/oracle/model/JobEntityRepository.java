package org.kie.kogito.index.oracle.model;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class JobEntityRepository implements PanacheRepositoryBase<JobEntity, String> {

}