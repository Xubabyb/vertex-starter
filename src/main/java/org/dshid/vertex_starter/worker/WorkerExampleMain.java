package org.dshid.vertex_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class WorkerExampleMain extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(WorkerExampleMain.class);
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    // Первый вариант выполнения блокирующего кода, внутри вертикла
    // требуемый фрагмент выполняется в worker-thread
    vertx.deployVerticle(new WorkerExampleMain());

    //Второй вариант целиком определить вертикл как worker вертикл
    vertx.deployVerticle(new WorkerVerticle(),
      new DeploymentOptions()
        .setThreadingModel(ThreadingModel.WORKER)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-threads")
    );
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());
    startPromise.complete(); //Выполняется в event-loop threads [vert.x-eventloop-thread-0]

    //Выполнение блокируемых вызовов
    vertx.executeBlocking(event -> {

        //Выполнение блокирующего вызова
        LOG.info("Executing blocking code");
        try {
          Thread.sleep(5000);
          event.complete(); //Выполняется в worker threads [vert.x-worker-thread-0]
        } catch (InterruptedException e) {
          LOG.error("Failed: ", e);
          event.fail(e);
        }
      },
      //Вернуть результат блокирующего вызова обратно в eveny-loop thread
      result -> {
        if (result.succeeded()){
          LOG.info("Blocking call done!"); //Выполняется в event-loop threads [vert.x-eventloop-thread-0]
        } else {
          LOG.info("Blocking call failed due to: ", result.cause());
        }
      });

  }
}
