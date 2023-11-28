package org.dshid.vertex_starter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


//Знакомство с Vertx
public class MainVerticle extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    LOG.debug("Start application");

    //Основной объект Vertx
    var vertx = Vertx.vertx();

    //Создание вертикла
    vertx.deployVerticle(new MainVerticle());
  }

  //Этот код сгенерирован платформой https://start.vertx.io
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.debug("Start {}", getClass().getSimpleName());
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello from Vert.x!");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOG.info("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
