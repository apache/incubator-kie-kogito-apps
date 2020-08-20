package org.kie.kogito.explainability;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.WebClient;
import org.eclipse.microprofile.context.ManagedExecutor;
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

public class RemoteKogitoPredictionProvider implements PredictionProvider {

    private final ExplainabilityRequest request;
    private final ThreadContext threadContext;
    private final ManagedExecutor managedExecutor;
    private final WebClient client;

    public RemoteKogitoPredictionProvider(ExplainabilityRequest request, Vertx vertx, ThreadContext threadContext,
                                          ManagedExecutor managedExecutor) {

        this.request = request;
        String serviceUrl = "http://localhost:8080";
//        String serviceUrl = request.getServiceUrl();
        URI uri = URI.create(serviceUrl);
        this.client = WebClient.create(vertx, new WebClientOptions().setDefaultHost(uri.getHost()).setDefaultPort(
                uri.getPort()).setSsl("https".equalsIgnoreCase(uri.getScheme())));
        this.threadContext = threadContext;
        this.managedExecutor = managedExecutor;
    }

    @Override
    public List<PredictionOutput> predict(List<PredictionInput> inputs) {
        List<PredictionOutput> predictionOutputs = new LinkedList<>();
        for (PredictionInput input : inputs) {
            Map<String, Object> map = toMap(input.getFeatures());
            PredictInput pi = new PredictInput();
            pi.setRequest(map);
            String[] namespaceAndName = extractNamespaceAndName(request.getExecutionId());
            pi.setModelIdentifier(new ModelIdentifier(namespaceAndName[0], namespaceAndName[1]));
            HttpRequest<Buffer> post = client.post("/predict");
            threadContext.withContextCapture(post.sendJson(pi).subscribeAsCompletionStage()).thenAcceptAsync(
                    r -> predictionOutputs.add(toPredictionOutput(r.bodyAsJsonObject())), managedExecutor);
        }
        return predictionOutputs;
    }

    private PredictionOutput toPredictionOutput(JsonObject json) {
        List<Output> outputs = new LinkedList<>();
        for (Map.Entry<String, Object> entry : json) {
            Output output = new Output(entry.getKey(), Type.UNDEFINED, new Value<>(entry.getValue()), 1d);
            outputs.add(output);
        }
        return new PredictionOutput(outputs);
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
