package org.dshid.vertex_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dshid.vertex_starter.verticles.VerticleA;
import org.dshid.vertex_starter.verticles.VerticleB;
import org.dshid.vertex_starter.verticles.VerticleExampleMain;

public class WorkerExampleMain extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(WorkerExampleMain.class);
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerExampleMain());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());
    startPromise.complete();

    //Выполнение блокируемых вызовов
    vertx.executeBlocking(event -> {

        //Выполнение блокирующего вызова
        LOG.info("Executing blocking code");
        try {
          Thread.sleep(5000);
          event.complete();
        } catch (InterruptedException e) {
          LOG.error("Failed: ", e);
          event.fail(e);
        }
      },
      //Вернуть результат блокирующего вызова обратно в eveny-loop thread
      result -> {
        if (result.succeeded()){
          LOG.info("Blocking call done!");
        } else {
          LOG.info("Blocking call failed due to: ", result.cause());
        }
      });

  }
}
