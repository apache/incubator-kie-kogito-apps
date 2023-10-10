package org.kie.kogito.persistence.postgresql.reporting.model.paths;

import org.kie.kogito.persistence.postgresql.reporting.model.JsonType;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresJsonField;
import org.kie.kogito.persistence.postgresql.reporting.model.PostgresMapping;
import org.kie.kogito.persistence.reporting.model.paths.PathSegment;
import org.kie.kogito.persistence.reporting.model.paths.TerminalPathSegment;

public class PostgresTerminalPathSegment extends TerminalPathSegment<JsonType, PostgresJsonField, PostgresMapping> {

    public PostgresTerminalPathSegment(final String segment,
            final PathSegment parent,
            final PostgresMapping mapping) {
        super(segment, parent, mapping);
    }
}
