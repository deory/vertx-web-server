package com.deory.vertxweb.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCallVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpCallVerticle.class);

    private static Vertx vertx;
    private static WebClient client;
    private static EventBus eventBus;

    public HttpCallVerticle(Vertx vertx) {
        HttpCallVerticle.vertx = vertx;
        client = WebClient.create(vertx);
        eventBus = HttpCallVerticle.vertx.eventBus();
    }

    @Override
    public void start() {
        logger.info("HttpCallVerticle start");

        eventBus.consumer("CallHttpBlocking-post", message -> {
            client.post(8080, "localhost", "/test")
                    .sendBuffer(Buffer.buffer(message.body().toString()))
                    .onSuccess(res -> {
//                                logger.info("res : {}", res.body());
                        message.reply(res.body());
                    });
        });

        eventBus.consumer("CallHttpBlocking-put", message -> {
            client.put(8080, "localhost", "/test")
                    .sendBuffer(Buffer.buffer(message.body().toString()))
                    .onSuccess(res -> {
//                                logger.info("res : {}", res.body());
                        message.reply(res.body());
                    });
        });
    }

    @Override
    public  void stop() {
        logger.info("HttpCallVerticle stop");
    }

}
