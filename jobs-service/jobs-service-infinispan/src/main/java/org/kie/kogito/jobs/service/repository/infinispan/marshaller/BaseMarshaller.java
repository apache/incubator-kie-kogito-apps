package org.kie.kogito.jobs.service.repository.infinispan.marshaller;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.infinispan.protostream.MessageMarshaller;

public abstract class BaseMarshaller<T> implements MessageMarshaller<T> {

    public static final String JOB_SERVICE_PKG = "job.service";

    public String getPackage() {
        return JOB_SERVICE_PKG;
    }

    public String mapEnum(Enum<?> e) {
        return Optional.ofNullable(e).map(Enum::name).orElse("");
    }

    public <E extends Enum<E>> E mapString(String input, Class<E> enumClass) {
        return Optional.ofNullable(input)
                .filter(StringUtils::isNotBlank)
                .map(s -> Enum.valueOf(enumClass, s))
                .orElse(null);
    }

    public Date fromInstant(Instant instant) {
        return Optional.ofNullable(instant)
                .map(Date::from)
                .orElse(null);
    }

    public Instant toInstant(Date date) {
        return Optional.ofNullable(date)
                .map(Date::toInstant)
                .orElse(null);
    }
}
