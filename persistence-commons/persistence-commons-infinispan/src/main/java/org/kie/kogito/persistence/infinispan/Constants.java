package org.kie.kogito.persistence.infinispan;

public class Constants {

    private Constants() {
    }

    public static final String INFINISPAN_STORAGE = "infinispan";

    // Copied from org.infinispan.query.remote.client.ProtobufMetadataManagerConstants to avoid the import of the entire module.
    public static final String PROTOBUF_METADATA_CACHE_NAME = "___protobuf_metadata";
}
