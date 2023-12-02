package org.dshid.vertex_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkerVerticle extends AbstractVerticle {
  private static final Logger LOG = LogManager.getLogger(WorkerVerticle.class);
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Deployed as worker verticle - {}", getClass().getSimpleName());
    startPromise.complete();
    Thread.sleep(5000);
    LOG.info("Blocking operation is done!");
  }
}
