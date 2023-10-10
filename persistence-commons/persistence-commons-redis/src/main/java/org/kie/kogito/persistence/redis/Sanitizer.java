package org.kie.kogito.persistence.redis;

public class Sanitizer {
    // Special chars to be escaped https://oss.redislabs.com/redisearch/Escaping/
    // asterisk ("*") is valid instead
    private static final String ILLEGAL_CHARS_REGEX = "[,.<>{}\\[\\]\"':;!@#$%^&()\\-+=~ ]";

    public static Object sanitize(Object o) {
        if (o instanceof String) {
            return ((String) o).replaceAll(ILLEGAL_CHARS_REGEX, "\\\\$0");
        }
        return o;
    }
}
