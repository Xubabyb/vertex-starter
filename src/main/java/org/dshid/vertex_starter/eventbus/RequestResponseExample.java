package org.dshid.vertex_starter.eventbus;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestResponseExample {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(RequestVerticle.class);
    static final String ADDRESS = "mu.request.address";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();

      final String message = "Hello World!";
      LOG.info("Sending: {}", message);

      eventBus.<String>request(           //Vertex не определяет схему адреса шины событий,
        ADDRESS,
        // нормальная практика использовать имя класса отправителя, но подойдет любая строка
        message,
        // второй параметр это сообщение, принимает: строки, объекты вертикла, пользовательские объекты
        reply -> {
          //  callback  на поступление ответа
          LOG.info("Response: {}", reply.result().body());

        });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    private static final Logger LOG = LogManager.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();

      vertx.eventBus().<String>consumer(RequestVerticle.ADDRESS,
        message -> {

          LOG.info("Received message: {}", message.body());

          message.reply("Successfully received message"); //Ответ на реквест

        });
    }
  }

}
