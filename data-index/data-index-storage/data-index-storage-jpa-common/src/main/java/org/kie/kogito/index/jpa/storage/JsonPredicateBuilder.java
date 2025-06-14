package org.kie.kogito.index.jpa.storage;

import org.kie.kogito.persistence.api.query.AttributeFilter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface JsonPredicateBuilder {
    Predicate buildPredicate(AttributeFilter<?> filter, Root<?> root,
            CriteriaBuilder builder);
}
