package org.kie.kogito.jitexecutor.dmn.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.kie.api.io.Resource;

public class ResolveByKey {
    private final Map<String, Resource> resources;

    public ResolveByKey(Map<String, Resource> resources) {
        this.resources = new HashMap<>(resources);
    }

    public Reader readerByKey(String key) {
        try {
            return resources.get(key).getReader();
        } catch (IOException e) {
            throw new RuntimeException("Unable to operate ValidatorImportReaderResolver", e);
        }
    }

    public Collection<Reader> allReaders() {
        List<Reader> results = resources.values().stream().map(r -> {
            try {
                return r.getReader();
            } catch (IOException e) {
                throw new RuntimeException("Unable to open reader for resource.", e);
            }
        }).collect(Collectors.toList());
        return results;
    }
}
