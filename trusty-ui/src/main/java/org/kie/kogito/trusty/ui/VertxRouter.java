package org.kie.kogito.trusty.ui;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;

@ApplicationScoped
public class VertxRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VertxRouter.class);

    @Location("index")
    Template indexTemplate;

    private String index;

    @PostConstruct
    public void init() {
        index = indexTemplate.render();
    }

    void setupRouter(@Observes Router router) {
        router.route("/").handler(ctx -> ctx.response().putHeader("location", "/audit").setStatusCode(302).end());
        router.route("/audit*").handler(ctx -> handle(ctx));
        router.route().handler(StaticHandler.create());
    }

    private void handle(RoutingContext context) {
        try {
            context.response()
                    .putHeader(HttpHeaders.CACHE_CONTROL, "no-cache")
                    .putHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf8")
                    .end(index);
        } catch (Exception ex) {
            LOGGER.error("Error handling index.html", ex);
            context.fail(500, ex);
        }
    }
}
