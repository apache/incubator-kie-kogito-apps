package org.kie.kogito.persistence.postgresql.model;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

@ApplicationScoped
public class CacheEntityRepository implements PanacheRepositoryBase<CacheEntity, CacheId> {
}
