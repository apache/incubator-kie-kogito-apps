package org.kie.kogito.swf.tools.custom.dashboard.model;

import java.util.ArrayList;
import java.util.List;

public class CustomDashboardFilter {

    private final List<String> names;

    public CustomDashboardFilter() {
        this.names = new ArrayList<>();
    }

    public CustomDashboardFilter(List<String> names) {
        this.names = names;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names.addAll(names);
    }

    @Override
    public String toString() {
        return "CustomDashboardFilter{" +
                "names=" + names +
                '}';
    }
}
