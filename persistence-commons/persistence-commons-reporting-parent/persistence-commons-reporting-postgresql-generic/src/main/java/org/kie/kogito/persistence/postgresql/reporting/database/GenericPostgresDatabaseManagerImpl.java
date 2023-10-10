package org.kie.kogito.persistence.postgresql.reporting.database;

import java.util.Objects;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.kie.kogito.persistence.postgresql.model.CacheEntityRepository;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresApplyMappingSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresIndexesSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTableSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTriggerDeleteSqlBuilder;
import org.kie.kogito.persistence.postgresql.reporting.database.sqlbuilders.PostgresTriggerInsertSqlBuilder;

@ApplicationScoped
public class GenericPostgresDatabaseManagerImpl extends BasePostgresDatabaseManagerImpl {

    private CacheEntityRepository repository;

    protected GenericPostgresDatabaseManagerImpl() {
        //CDI proxy
    }

    @Inject
    public GenericPostgresDatabaseManagerImpl(final CacheEntityRepository repository,
            final PostgresIndexesSqlBuilder indexesSqlBuilder,
            final PostgresTableSqlBuilder tableSqlBuilder,
            final PostgresTriggerDeleteSqlBuilder triggerDeleteSqlBuilder,
            final PostgresTriggerInsertSqlBuilder triggerInsertSqlBuilder,
            final PostgresApplyMappingSqlBuilder applyMappingSqlBuilder) {
        super(indexesSqlBuilder,
                tableSqlBuilder,
                triggerDeleteSqlBuilder,
                triggerInsertSqlBuilder,
                applyMappingSqlBuilder);
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    protected EntityManager getEntityManager(final String sourceTableName) {
        return repository.getEntityManager();
    }

}
