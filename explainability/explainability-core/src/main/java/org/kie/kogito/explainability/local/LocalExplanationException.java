package org.kie.kogito.explainability.local;

/**
 * Exception representing errors happened during the process of generating a local explanation.
 */
public class LocalExplanationException extends Exception {

    public LocalExplanationException(String message) {
        super(message);
    }

}
