package org.dshid.vertex_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Благодаря этому подходу мы можем осуществлять потокобезопасное взаимодействие между вертиклами,
 * и нам не нужно беспокоиться о проблемах параллелизма.
 */
public class PointToPointExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new Sender());
    vertx.deployVerticle(new Receiver());
  }

  static class Sender extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(Sender.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();

      final String message = "New message....";
      //Задаем периодичность отправки сообщений
      vertx.setPeriodic(
        1000,                                       // задержка
        id -> {                                           // обработчик

          LOG.info("Sending: {}", message);
          // отправка сообщения
          vertx.eventBus()
            .send(
              Sender.class.getName(),       //Адрес (любая строка)
              "Sending a message...."       // Сообщение
            );
        }
      );
    }
  }

  static class Receiver extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(Receiver.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();

      // получение сообщения
      vertx.eventBus()
        .<String>consumer(
          Sender.class.getName(),       //Адрес
          message -> {                  //Обработчик

            LOG.info("Received message: {}", message.body());
          }

        );

    }
  }
}
