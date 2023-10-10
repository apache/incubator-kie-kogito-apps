package org.kie.kogito.persistence.redis;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {

    public static final String NAME_PROPERTY = "name";
    public static final String AGE_PROPERTY = "age";
    public static final String BIOGRAPHY_PROPERTY = "biography";

    @JsonProperty(NAME_PROPERTY)
    public String name;

    @JsonProperty(AGE_PROPERTY)
    public Integer age;

    @JsonProperty(BIOGRAPHY_PROPERTY)
    public String biography;

    public Person() {
    }

    public Person(String name, Integer age) {
        this(name, age, "");
    }

    public Person(String name, Integer age, String biography) {
        this.name = name;
        this.age = age;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getBiography() {
        return biography;
    }
}
