package org.kie.kogito.index.addon.vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.arc.properties.IfBuildProperty;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;

@ApplicationScoped
@IfBuildProperty(name = "quarkus.kogito.data-index.graphql.ui.always-include", stringValue = "true")
public class VertxGraphiQLSetup {

    @Inject
    @ConfigProperty(name = "quarkus.http.non-application-root-path")
    String path;

    @Inject
    @ConfigProperty(name = "quarkus.http.root-path")
    String rootPath;

    void setupRouter(@Observes Router router) {
        GraphiQLHandler graphiQLHandler = GraphiQLHandler.create(new GraphiQLHandlerOptions().setEnabled(true));
        router.route(rootPath + path + "/graphql-ui/*").handler(graphiQLHandler);
    }

}
