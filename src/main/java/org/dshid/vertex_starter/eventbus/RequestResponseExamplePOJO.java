package org.dshid.vertex_starter.eventbus;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dshid.vertex_starter.eventbus.codec.LocalMessageCodec;
import org.dshid.vertex_starter.eventbus.dto.Ping;
import org.dshid.vertex_starter.eventbus.dto.Pong;

/*
 * По умолчанию Vertex не позволяет отправлять пользовательские объекты Java по шине событий.
 * Для этого  нужен специальный кодек сообщений.
 */
public class RequestResponseExamplePOJO {

  private static final Logger LOG = LogManager.getLogger(RequestResponseExamplePOJO.class);


  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), logOnError());
    vertx.deployVerticle(new PongVerticle(), logOnError());
  }

  // хэндлеры ошибок
  private static Handler<AsyncResult<String>> logOnError() {
    return asyncResult -> {
      if (asyncResult.failed()) {
        LOG.error("err", asyncResult.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(PingVerticle.class);
    static final String ADDRESS = PingVerticle.class.getName();

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      var eventBus = vertx.eventBus();
      final Ping message = new Ping("Hello", true);
      LOG.info("Sending: {}", message);

      //Регистрируем кодек
      eventBus.registerDefaultCodec(Ping.class, new LocalMessageCodec<>(Ping.class));
      // Если попробовать выполнить без кодеков то получим эксепшен
      // No message codec for type: class org.dshid.vertex_starter.eventbus.dto.Ping
      eventBus.<Pong>request(ADDRESS, message,
        reply -> {
          if (reply.failed()) {
            LOG.error("Failed: ", reply.cause());
            return;
          }
          LOG.info("Response: {}", reply.result().body());

        });
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

      //Регистрируем кодек
      vertx.eventBus().registerDefaultCodec(Pong.class, new LocalMessageCodec<>(Pong.class));
      vertx.eventBus().<Ping>consumer(PingVerticle.class.getName(),
        message -> {

          LOG.info("Received message: {}", message.body());

          message.reply(new Pong(1)); //Ответ на реквест

        }).exceptionHandler(e -> LOG.error("Callback fail: ", e));
      startPromise.complete();
    }
  }

}
