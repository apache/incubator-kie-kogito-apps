package org.kie.kogito;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHelloService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHelloService.class);

    public String hello(String input) throws InterruptedException {
        //Add artificial delay to avoid knative http completing the callback too fast
        TimeUnit.SECONDS.sleep(1l);
        LOGGER.info("Executing {}.hello(\"{}\")", getClass().getName(), input);
        return "Hello " + input;
    }

    public String bye(String input) {
        LOGGER.info("Executing {}.bye(\"{}\")", getClass().getName(), input);
        return "Bye " + input;
    }

    public String helloJson(String input) {
        LOGGER.info("Executing {}.helloJson(\"{}\")", getClass().getName(), input);
        return "{\"answer\" : \"Hello " + input + "\"}";
    }
}
