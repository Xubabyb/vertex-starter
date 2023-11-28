package org.dshid.vertex_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class VerticleExampleMain extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(VerticleExampleMain.class);
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new VerticleExampleMain());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());

    // деплой дочерних вертикалей
    vertx.deployVerticle(new VerticleA());
    vertx.deployVerticle(new VerticleB());

    // обратный вызов сигнализирующий о запуске вертикали
    startPromise.complete();
  }
}
