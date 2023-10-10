package org.kie.kogito.swf.tools.custom.dashboard.converter;

import java.util.Collections;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.Provider;

import org.kie.kogito.swf.tools.custom.dashboard.model.CustomDashboardFilter;

@Provider
public class CustomDashboardFilterParamConverter implements ParamConverter<CustomDashboardFilter> {
    public CustomDashboardFilter fromString(String names) {
        StringTokenizer stringTokenizer = new StringTokenizer(names, ";");
        return new CustomDashboardFilter(Collections.list(stringTokenizer).stream().map(s -> (String) s).collect(Collectors.toList()));
    }

    public String toString(CustomDashboardFilter names) {
        return names.toString();
    }
}
