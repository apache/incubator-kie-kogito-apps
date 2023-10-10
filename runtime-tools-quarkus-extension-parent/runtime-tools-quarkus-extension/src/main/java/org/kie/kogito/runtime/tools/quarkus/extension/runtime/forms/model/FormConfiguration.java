package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model;

public class FormConfiguration {

    private String schema;
    private FormResources resources;

    public FormConfiguration() {
    }

    public FormConfiguration(String schema, FormResources resources) {
        this.schema = schema;
        this.resources = resources;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public FormResources getResources() {
        return resources;
    }

    public void setResources(FormResources resources) {
        this.resources = resources;
    }
}
