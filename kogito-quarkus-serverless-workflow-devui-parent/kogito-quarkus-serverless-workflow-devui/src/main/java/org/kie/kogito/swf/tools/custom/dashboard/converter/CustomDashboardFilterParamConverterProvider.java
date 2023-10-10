package org.kie.kogito.swf.tools.custom.dashboard.converter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;

import org.kie.kogito.swf.tools.custom.dashboard.model.CustomDashboardFilter;

@Provider
public class CustomDashboardFilterParamConverterProvider implements ParamConverterProvider {

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.isAssignableFrom(CustomDashboardFilter.class)) {
            return (ParamConverter<T>) new CustomDashboardFilterParamConverter();
        }
        return null;
    }
}