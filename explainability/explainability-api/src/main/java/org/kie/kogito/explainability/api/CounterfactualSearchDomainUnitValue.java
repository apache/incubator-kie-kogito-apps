package org.kie.kogito.explainability.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CounterfactualSearchDomainUnitValue extends CounterfactualSearchDomainValue {

    public static final String BASE_TYPE = "baseType";
    public static final String FIXED = "fixed";
    public static final String DOMAIN = "domain";

    @JsonProperty(BASE_TYPE)
    @JsonInclude(NON_NULL)
    private String baseType;

    @JsonProperty(FIXED)
    private Boolean isFixed;

    @JsonProperty(DOMAIN)
    private CounterfactualDomain domain;

    private CounterfactualSearchDomainUnitValue() {
    }

    public CounterfactualSearchDomainUnitValue(String type,
            String baseType,
            Boolean isFixed,
            CounterfactualDomain domain) {
        super(Kind.UNIT, type);
        this.baseType = baseType;
        this.isFixed = isFixed;
        this.domain = domain;
    }

    public String getBaseType() {
        return baseType;
    }

    public Boolean isFixed() {
        return isFixed;
    }

    public CounterfactualDomain getDomain() {
        return domain;
    }

}
