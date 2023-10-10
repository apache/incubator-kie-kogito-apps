package org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.kie.kogito.runtime.tools.quarkus.extension.runtime.forms.model.FormFilter;

@Provider
public class FormFilterParamConverterProvider implements ParamConverterProvider {

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.isAssignableFrom(FormFilter.class)) {
            return (ParamConverter<T>) new FormFilterParamConverter();
        }
        return null;
    }
}