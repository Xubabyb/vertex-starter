package org.dshid.vertex_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerticleA extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(VerticleA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());

    vertx.deployVerticle(new VerticleAA(), whenDeploy -> {
      LOG.info("Deployed - {}", VerticleAA.class.getSimpleName());
      // вызов метода stop
      vertx.undeploy(whenDeploy.result());
    });

    vertx.deployVerticle(new VerticleAB(), whenDeploy -> {
      LOG.info("Deployed - {}", VerticleAB.class.getSimpleName());
      //Не сворачиваем деплой
    });
    startPromise.complete();
  }
}
