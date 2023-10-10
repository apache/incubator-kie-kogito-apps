package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

public class FormContent {

    private String source;
    private FormConfiguration configuration;

    public FormContent() {
    }

    public FormContent(String source, FormConfiguration configuration) {
        this.source = source;
        this.configuration = configuration;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setConfiguration(FormConfiguration configuration) {
        this.configuration = configuration;
    }

    public String getSource() {
        return source;
    }

    public FormConfiguration getConfiguration() {
        return configuration;
    }
}
