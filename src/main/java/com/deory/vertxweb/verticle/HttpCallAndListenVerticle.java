package com.deory.vertxweb.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCallAndListenVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpCallAndListenVerticle.class);

    private static Vertx vertx;
    private static HttpServer server;
    private static Router router;
    private static WebClient client;

    public HttpCallAndListenVerticle(Vertx vertx) {
        HttpCallAndListenVerticle.vertx = vertx;
        server = HttpCallAndListenVerticle.vertx.createHttpServer();
        router = Router.router(vertx);
        client = WebClient.create(vertx);
    }

    @Override
    public void start() {
        logger.info("HttpCallAndListenVerticle start - HttpServer Listen on 8082");

        router.post().handler(BodyHandler.create())
                .handler(ctx -> {
                    HttpServerResponse response = ctx.response();

                    client.post(8080, "localhost", "/test")
                            .sendBuffer(ctx.getBody())
                            .onSuccess(res -> {
//                                logger.info("post response body : {}", res.body());
                                response.end(res.body());
                            });
                });

        router.put().handler(BodyHandler.create())
                .handler(ctx -> {
                    HttpServerResponse response = ctx.response();

                    client.put(8080, "localhost", "/test")
                            .sendBuffer(ctx.getBody())
                            .onSuccess(res -> {
//                                logger.info("put response body : {}", res.body());
                                response.end(res.body());
                            });
                });

        server.requestHandler(router).listen(8082);
    }

    @Override
    public  void stop() {
        logger.info("HttpCallAndListenVerticle stop");
    }

}
