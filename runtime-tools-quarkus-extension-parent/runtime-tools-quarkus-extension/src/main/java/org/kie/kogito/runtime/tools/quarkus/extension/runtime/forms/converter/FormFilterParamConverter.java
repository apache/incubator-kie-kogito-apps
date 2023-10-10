package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.converter;

import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.Provider;

import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.FormFilter;

@Provider
public class FormFilterParamConverter implements ParamConverter<FormFilter> {
    public FormFilter fromString(String names) {
        StringTokenizer stringTokenizer = new StringTokenizer(names, ";");
        return new FormFilter(Collections.list(stringTokenizer).stream().map(s -> (String) s).collect(Collectors.toList()));
    }

    public String toString(FormFilter names) {
        return names.toString();
    }
}
