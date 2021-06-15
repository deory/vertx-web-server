package com.deory.vertxweb;

import com.deory.vertxweb.verticle.HttpCallAndListenVerticle;
import com.deory.vertxweb.verticle.HttpCallVerticle;
import com.deory.vertxweb.verticle.HttpListenVerticle;
import io.vertx.core.Vertx;

public class VertxWebApplication {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new HttpCallVerticle(vertx));
        vertx.deployVerticle(new HttpListenVerticle(vertx));

        vertx.deployVerticle(new HttpCallAndListenVerticle(vertx));
    }

}
