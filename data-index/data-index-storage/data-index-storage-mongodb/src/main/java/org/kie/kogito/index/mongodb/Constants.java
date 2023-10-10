package org.kie.kogito.index.mongodb;

public class Constants {

    private Constants() {
    }

    private static final String DOMAIN_COLLECTON_NAME_AFFIX = "_domain";

    public static String getDomainCollectionName(String processId) {
        return processId + DOMAIN_COLLECTON_NAME_AFFIX;
    }

    public static boolean isDomainCollection(String collection) {
        return collection.endsWith(DOMAIN_COLLECTON_NAME_AFFIX);
    }
}
