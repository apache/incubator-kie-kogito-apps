package org.kie.kogito.persistence.oracle.model;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class CacheEntityRepository implements PanacheRepositoryBase<CacheEntity, CacheId> {
}
