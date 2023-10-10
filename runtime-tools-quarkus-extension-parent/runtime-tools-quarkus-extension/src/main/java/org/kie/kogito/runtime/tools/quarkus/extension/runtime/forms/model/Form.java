package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

public class Form {

    private final FormInfo formInfo;
    private final String source;
    private final FormConfiguration configuration;

    public Form(FormInfo formInfo, String source, FormConfiguration configuration) {
        this.formInfo = formInfo;
        this.source = source;
        this.configuration = configuration;
    }

    public FormInfo getFormInfo() {
        return formInfo;
    }

    public String getSource() {
        return source;
    }

    public FormConfiguration getConfiguration() {
        return configuration;
    }
}
