package org.kie.kogito.explainability;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

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

import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

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
        String[] namespaceAndName = Stream.of(
                request.getModelNamespace(),
                request.getModelName()
        ).toArray(String[]::new);

        return inputs.stream()
                .map(input -> sendPredictRequest(input, namespaceAndName))
                .reduce(completedFuture(emptyList()),
                        (cf1, cf2) -> cf1.thenCombine(cf2, this::addElement),
                        (cf1, cf2) -> cf1.thenCombine(cf2, this::merge));
    }

    private PredictionOutput toPredictionOutput(JsonObject json) {
        List<Output> outputs = new LinkedList<>();
        for (Map.Entry<String, Object> entry : json) {
            Output output = new Output(entry.getKey(), Type.UNDEFINED, new Value<>(entry.getValue()), 1d);
            outputs.add(output);
        }
        return new PredictionOutput(outputs);
    }

    private List<PredictionOutput> addElement(List<PredictionOutput> l1, PredictionOutput elem) {
        List<PredictionOutput> result = new ArrayList<>(l1);
        result.add(elem);
        return result;
    }

    private List<PredictionOutput> merge(List<PredictionOutput> l1, List<PredictionOutput> l2) {
        List<PredictionOutput> result = new ArrayList<>();
        result.addAll(l1);
        result.addAll(l2);
        return result;
    }

    private CompletableFuture<PredictionOutput> sendPredictRequest(PredictionInput input, String[] namespaceAndName) {
        ModelIdentifier modelIdentifier = new ModelIdentifier("dmn", String.join(":", namespaceAndName));
        PredictInput pi = new PredictInput(modelIdentifier, toMap(input.getFeatures()));
        List<PredictInput> piList = List.of(pi);

        LOG.warn(Json.encodePrettily(piList));

        return client.post("/predict")
                .sendJson(piList)
                .subscribeAsCompletionStage()
                .thenApplyAsync(r -> {
                    r.headers().forEach(entry -> LOG.warn("[HEADER] {}: {}", entry.getKey(), entry.getValue()));
                    LOG.warn("[BODY] {}", r.bodyAsString());
                    return toPredictionOutput(r.bodyAsJsonObject());
                }, asyncExecutor);
    }

    private String[] extractNamespaceAndName(String resourceId) {
        int index = resourceId.lastIndexOf(ModelIdentifier.RESOURCE_ID_SEPARATOR);
        if (index < 0 || index == resourceId.length()) {
            throw new IllegalArgumentException("Malformed resourceId " + resourceId);
        }
        return new String[]{resourceId.substring(0, index), resourceId.substring(index + 1)};
    }

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
