Ensure migration scripts are developed to support several executions over the same database without any error.
This feature will make sure this migration execution would be compatible with other needed flyway migrations without broking the chain.

IMPORTANT: `data-index-storage-jpa` relies in some migrations present in `persistence-commons-jpa` module
that initialize the cache for domain objects (kogito_data_cache), please make sure that new migrations don't collide
between this two modules