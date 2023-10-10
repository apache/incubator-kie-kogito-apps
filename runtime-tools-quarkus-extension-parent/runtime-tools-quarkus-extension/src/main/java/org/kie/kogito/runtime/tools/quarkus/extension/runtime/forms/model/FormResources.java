package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class FormResources {

    private Map<String, String> scripts = new LinkedHashMap<>();

    private Map<String, String> styles = new LinkedHashMap<>();

    public FormResources() {
    }

    public Map<String, String> getScripts() {
        return scripts;
    }

    public void setScripts(Map<String, String> scripts) {
        this.scripts = scripts;
    }

    public Map<String, String> getStyles() {
        return styles;
    }

    public void setStyles(Map<String, String> styles) {
        this.styles = styles;
    }
}
