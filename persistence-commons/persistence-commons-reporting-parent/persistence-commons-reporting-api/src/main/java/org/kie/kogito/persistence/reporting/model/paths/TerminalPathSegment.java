package org.kie.kogito.persistence.reporting.model.paths;

import org.kie.kogito.persistence.reporting.model.JsonField;
import org.kie.kogito.persistence.reporting.model.Mapping;

public class TerminalPathSegment<T, J extends JsonField<T>, M extends Mapping<T, J>> extends PathSegment {

    private final M mapping;

    public TerminalPathSegment(final String segment,
            final PathSegment parent,
            final M mapping) {
        super(segment, parent);
        this.mapping = mapping;
    }

    public M getMapping() {
        return mapping;
    }
}
