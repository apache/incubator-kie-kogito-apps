package org.kie.kogito.persistence.redis;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.redisearch.SearchResult;
import io.redisearch.client.Client;
import org.kie.kogito.persistence.api.query.AttributeFilter;
import org.kie.kogito.persistence.api.query.AttributeSort;
import org.kie.kogito.persistence.api.query.Query;
import org.kie.kogito.persistence.api.query.SortDirection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisQuery<V> implements Query<V> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisQuery.class);

    Integer limit;
    Integer offset;
    List<AttributeFilter<?>> filters;
    List<AttributeSort> sortBy;
    ObjectMapper mapper = new ObjectMapper();

    private Class<V> type;

    private Client redisClient;

    public RedisQuery(Client redisClient, Class<V> type) {
        this.redisClient = redisClient;
        this.type = type;
    }

    @Override
    public Query<V> limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query<V> offset(Integer offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Query<V> filter(List<AttributeFilter<?>> filters) {
        this.filters = filters;
        return this;
    }

    @Override
    public Query<V> sort(List<AttributeSort> sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @Override
    public List<V> execute() {
        LOGGER.info(RedisQueryFactory.buildQueryBody(filters));
        io.redisearch.Query query = new io.redisearch.Query(RedisQueryFactory.buildQueryBody(filters));
        if (limit != null && offset != null) {
            query.limit(limit, offset);
        }
        if (sortBy.size() == 1){ // TODO: implement backend side sorting on multiple attributes
            query.setSortBy(sortBy.get(0).getAttribute(), SortDirection.ASC.equals(sortBy.get(0).getSort()));
        }
        if (sortBy.size() > 1){
            throw new UnsupportedOperationException("Unsupported multiple sorting attributes");
        }

        RedisQueryFactory.addFilters(query, filters);

        SearchResult search = redisClient.search(query);
        LOGGER.info("total results: " + search.totalResults);
        return search.docs.stream().map(x -> {
            try {
                return mapper.readValue((String)x.get("rawObject"), type);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    List<AttributeSort> getSortBy() {
        return sortBy;
    }
}
