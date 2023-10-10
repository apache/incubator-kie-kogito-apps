package org.kie.kogito.index.infinispan.protostream;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class TestUtils {

    private TestUtils() {
    }

    public static String getTestProtoBufferFile() throws Exception {
        return readFileContent("test.proto");
    }

    public static String readFileContent(String file) throws IOException {
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(file);
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                ByteArrayOutputStream buf = new ByteArrayOutputStream()) {
            Objects.requireNonNull(inputStream, "Could not resolve file path: " + file);
            int result = bis.read();
            while (result != -1) {
                buf.write((byte) result);
                result = bis.read();
            }
            return buf.toString(StandardCharsets.UTF_8.name());
        }
    }

}
