package org.dshid.vertex_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Одна из возможностей общения по event bus — это публикация, подписка, общение. Одна вертикаль
 * публикует сообщение, которое может быть получено несколькими потребителями одновременно. Можно
 * рассматривать это как широковещательное сообщение.
 */
public class PublishSubscribeExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Publisher());
    vertx.deployVerticle(new Subscriber1());
    vertx.deployVerticle(Subscriber2.class.getName(), new DeploymentOptions().setInstances(2));

  }

  public static class Publisher extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(Publisher.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      //Задаем периодичность публикации сообщений
      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id ->
      {
        vertx.eventBus().publish(Publisher.class.getName(), "message for everyone!");
        LOG.info("Publication is done!");
      });
    }
  }

  public static class Subscriber1 extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(Subscriber1.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publisher.class.getName(),
        message -> {
          LOG.info("Received message: {}", message.body());
        }
      );
    }
  }

  public static class Subscriber2 extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(Subscriber2.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().<String>consumer(Publisher.class.getName(),
        message -> {
          LOG.info("Received message: {}", message.body());
        }
      );
    }
  }
}
