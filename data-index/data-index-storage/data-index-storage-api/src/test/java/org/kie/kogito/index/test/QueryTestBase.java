package org.kie.kogito.index.test;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.AfterEach;
import org.kie.kogito.persistence.api.Storage;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.AttributeSort;

public abstract class QueryTestBase<K, V> {

    public abstract Storage<K, V> getStorage();

    protected Boolean isDateTimeAsLong() {
        return true;
    }

    @AfterEach
    void tearDown() {
        getStorage().clear();
    }

    public void queryAndAssert(BiConsumer<List<V>, String[]> assertConsumer, Storage<K, V> storage, List<AttributeFilter<?>> filters, List<AttributeSort> sort, Integer offset, Integer limit,
            String... ids) {
        assertConsumer.accept(storage.query().filter(filters).sort(sort).offset(offset).limit(limit).execute(), ids);
    }

    protected Object getDateTime(ZonedDateTime dateTime) {
        return isDateTimeAsLong() ? dateTime.toInstant().toEpochMilli() : dateTime;
    }

    protected Object getDateTime() {
        return isDateTimeAsLong() ? Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli() : ZonedDateTime.now().plus(1, ChronoUnit.DAYS);
    }
}
