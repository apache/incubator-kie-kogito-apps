package org.kie.kogito.persistence.reporting.model.paths;

public class JoinPathSegment extends PathSegment {

    private final String groupName;

    public JoinPathSegment(final String segment,
            final PathSegment parent,
            final String groupName) {
        super(segment, parent);
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }
}
