package org.dshid.vertex_starter.scaling;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerticleN extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(VerticleN.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());
    LOG.debug("Config {}", config().toString());
    startPromise.complete();
  }
}
