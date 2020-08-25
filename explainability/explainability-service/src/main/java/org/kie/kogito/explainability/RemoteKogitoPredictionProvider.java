package org.kie.kogito.explainability;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.eclipse.microprofile.context.ThreadContext;
import org.kie.kogito.explainability.model.Feature;
import org.kie.kogito.explainability.model.Output;
import org.kie.kogito.explainability.model.PredictionInput;
import org.kie.kogito.explainability.model.PredictionOutput;
import org.kie.kogito.explainability.model.PredictionProvider;
import org.kie.kogito.explainability.model.Type;
import org.kie.kogito.explainability.model.Value;
import org.kie.kogito.explainability.models.ExplainabilityRequest;
import org.kie.kogito.explainability.models.ModelIdentifier;
import org.kie.kogito.explainability.models.PredictInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static java.util.stream.Collectors.toList;

public class RemoteKogitoPredictionProvider implements PredictionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(RemoteKogitoPredictionProvider.class);

    private final ExplainabilityRequest request;
    private final ThreadContext threadContext;
    private final Executor asyncExecutor;
    private final WebClient client;

    public RemoteKogitoPredictionProvider(ExplainabilityRequest request, Vertx vertx, ThreadContext threadContext, Executor asyncExecutor) {
        this.request = request;
        URI uri = URI.create(request.getServiceUrl());
        this.client = WebClient.create(vertx, new WebClientOptions()
                .setDefaultHost(uri.getHost())
                .setDefaultPort(uri.getPort())
                .setSsl("https".equalsIgnoreCase(uri.getScheme()))
                .setLogActivity(true)
        );
        this.threadContext = threadContext;
        this.asyncExecutor = asyncExecutor;
    }

    @Override
    public CompletableFuture<List<PredictionOutput>> predict(List<PredictionInput> inputs) {
        return sendPredictRequest(inputs, request.getModelIdentifier());
    }

    private PredictionOutput toPredictionOutput(JsonObject json) {
        List<Output> outputs = new LinkedList<>();
        for (Map.Entry<String, Object> entry : json) {
            Output output = new Output(entry.getKey(), Type.UNDEFINED, new Value<>(entry.getValue()), 1d);
            outputs.add(output);
        }
        return new PredictionOutput(outputs);
    }

    private CompletableFuture<List<PredictionOutput>> sendPredictRequest(List<PredictionInput> inputs, ModelIdentifier modelIdentifier) {
        List<PredictInput> piList = inputs.stream()
                .map(input -> new PredictInput(modelIdentifier, toMap(input.getFeatures())))
                .collect(toList());

        LOG.warn(Json.encodePrettily(piList));

        return threadContext.withContextCapture(client.post("/predict")
                .sendJson(piList)
                .subscribeAsCompletionStage())
                .thenApplyAsync(r -> {
                    r.headers().forEach(entry -> LOG.warn("[HEADER] {}: {}", entry.getKey(), entry.getValue()));
                    LOG.warn("[BODY] {}", r.bodyAsString());
                    // FIXME handle parsing errors
                    return r.bodyAsJsonObject().stream()
                            .map(json -> toPredictionOutput((JsonObject) json))
                            .collect(toList());
                }, asyncExecutor);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(List<Feature> features) {
        Map<String, Object> map = new HashMap<>();
        for (Feature f : features) {
            if (Type.COMPOSITE.equals(f.getType())) {
                List<Feature> compositeFeatures = (List<Feature>) f.getValue().getUnderlyingObject();
                Map<String, Object> maps = new HashMap<>();
                for (Feature cf : compositeFeatures) {
                    Map<String, Object> compositeFeatureMap = toMap(List.of(cf));
                    maps.putAll(compositeFeatureMap);
                }
                map.put(f.getName(), maps);
            } else {
                if (Type.UNDEFINED.equals(f.getType())) {
                    Feature underlyingFeature = (Feature) f.getValue().getUnderlyingObject();
                    map.put(f.getName(), toMap(List.of(underlyingFeature)));
                } else {
                    Object underlyingObject = f.getValue().getUnderlyingObject();
                    map.put(f.getName(), underlyingObject);
                }
            }
        }
        if (map.containsKey("context")) {
            map = (Map<String, Object>) map.get("context");
        }
        return map;
    }
}
