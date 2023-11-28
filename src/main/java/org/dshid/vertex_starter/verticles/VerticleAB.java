package org.dshid.vertex_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerticleAB extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(VerticleAB.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    LOG.info("Stop verticle - {}", getClass().getSimpleName());
    stopPromise.complete();
  }
}
