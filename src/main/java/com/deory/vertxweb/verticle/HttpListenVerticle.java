package com.deory.vertxweb.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpListenVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpListenVerticle.class);

    private static Vertx vertx;
    private static Router router;
    private static HttpServer server;
    private static EventBus eventBus;

    public HttpListenVerticle(Vertx vertx) {
        HttpListenVerticle.vertx = vertx;
        server = HttpListenVerticle.vertx.createHttpServer();
        router = Router.router(vertx);
        eventBus = HttpListenVerticle.vertx.eventBus();
    }

    @Override
    public void start() {
        logger.info("HttpListenVerticle start - HttpServer Listen on 8081");

        router.post().handler(BodyHandler.create())
                .handler(ctx -> {
                    HttpServerResponse response = ctx.response();

                    eventBus.request("CallHttpBlocking-post", ctx.getBody(), reply -> {
                        if (reply.succeeded()) {
                            response.end(reply.result().body().toString());
                        }
                    });
                });

        router.put().handler(BodyHandler.create())
                .handler(ctx -> {
                    HttpServerResponse response = ctx.response();

                    eventBus.request("CallHttpBlocking-put", ctx.getBody(), reply -> {
                        if (reply.succeeded()) {
                            response.end(reply.result().body().toString());
                        }
                    });
                });

        server.requestHandler(router).listen(8081);
    }

    @Override
    public  void stop() {
        logger.info("HttpListenVerticle stop");
    }

}
