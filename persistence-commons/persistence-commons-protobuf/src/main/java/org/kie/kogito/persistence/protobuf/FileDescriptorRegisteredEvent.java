package org.kie.kogito.persistence.protobuf;

import org.infinispan.protostream.descriptors.FileDescriptor;

public class FileDescriptorRegisteredEvent {

    private final FileDescriptor descriptor;

    public FileDescriptorRegisteredEvent(FileDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    public FileDescriptor getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return "FileDescriptorRegisteredEvent{" +
                "descriptor=" + descriptor +
                '}';
    }
}
