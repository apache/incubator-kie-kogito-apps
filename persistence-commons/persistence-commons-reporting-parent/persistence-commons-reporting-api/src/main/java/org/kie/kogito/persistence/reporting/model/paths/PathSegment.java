package org.kie.kogito.persistence.reporting.model.paths;

import java.util.ArrayList;
import java.util.List;

public class PathSegment {

    private final String segment;
    private final PathSegment parent;
    private final List<PathSegment> children;

    public PathSegment(final String segment,
            final PathSegment parent) {
        this.segment = segment;
        this.children = new ArrayList<>();
        this.parent = parent;
    }

    public String getSegment() {
        return segment;
    }

    public PathSegment getParent() {
        return parent;
    }

    public List<PathSegment> getChildren() {
        return children;
    }
}
