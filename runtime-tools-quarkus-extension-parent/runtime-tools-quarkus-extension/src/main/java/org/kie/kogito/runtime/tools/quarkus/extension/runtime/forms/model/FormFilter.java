package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

import java.util.ArrayList;
import java.util.List;

public class FormFilter {

    private final List<String> names;

    public FormFilter() {
        this.names = new ArrayList<>();
    }

    public FormFilter(List<String> names) {
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
        return "FormFilter{" +
                "names=" + names +
                '}';
    }
}
