package org.dshid.vertex_starter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestFuturePromiseExample {

  private static final Logger LOG = LogManager.getLogger(TestFuturePromiseExample.class);

  @Test
  void promise_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("START");

    vertx.setTimer(500,
      id -> {         //обработчик в таймере выполняется асинхронно с задержкой в полсекунды.
        promise.complete(
          "Success!");     // это хорошо видно влогах теста, т.о. выполнение теста не зависит от обещание (promise)
        LOG.debug("Success!");                // см. также тест promise_failure
        context.completeNow();                // для того, чтобы понять обещании , необходимо Future, то есть read операция
      });

    LOG.debug("END");
  }

  @Test
  void promise_failure(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("START");

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Failed!");
      context.completeNow();
    });

    LOG.debug("END");
  }

  //Стоит обратить внимание на последлвательность записей логов  :))
  @Test
  void future_success(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("START");

    vertx.setTimer(500, id -> {
      promise.complete("Success!");        //Выполняется в цикле событий
      LOG.debug("Success!");
    });

    final Future<String> future = promise.future();
    future
      .onSuccess(result -> {                     //Выполняется в цикле событий
        LOG.debug("Result {}", result);
        context.completeNow();
      })
      .onFailure(context::failNow);

    LOG.debug("END");
  }


  @Test
  void future_failed(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("START");

    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed!"));
      LOG.debug("Timer done!");
    });

    final Future<String> future = promise.future();
    future
      .onSuccess(result -> {
        LOG.debug("Result {}", result);
        context.completeNow();
      })
      .onFailure(error -> {
        LOG.debug("Error: ", error);
        context.completeNow();
      });

    LOG.debug("END");
  }

  //Future также используются для обработки результата Promise,
  // а не только для обработки успеха или неудачи.
  @Test
  void future_map(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();
    LOG.debug("START");

    vertx.setTimer(500, id -> {
      promise.complete("Success!");
      LOG.debug("Timer done!");
    });

    final Future<String> future = promise.future();
    future
      .map(asString -> {
        LOG.debug("Map String to JsonObject");
        return new JsonObject().put("key", asString);
      })
      .map(jsonObject -> {
        LOG.debug("Map JsonObject to JsonArray");
        return new JsonArray().add(jsonObject);
      })
      .onSuccess(result -> {
        LOG.debug("Result {} of type {}", result, result.getClass().getSimpleName());
        context.completeNow();
      })
      .onFailure(context::failNow);

    LOG.debug("END");
  }

  //Координирование асинхронных задачь в последовательность
  @Test
  void future_coordination(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(request -> LOG.debug("request {}", request))
      .listen(10_000)
      .compose(server -> {
        LOG.debug("Another task");
        return Future.succeededFuture(server); //возвращаем Future от исходного объекта Future
      })
      .compose(server -> {
        LOG.debug("Even more");
        return Future.succeededFuture(server); //возвращаем Future от исходного объекта Future
      })
      .onFailure(context::failNow)
      .onSuccess(server -> {
        LOG.debug("Server started on port {}", server.actualPort());
        context.completeNow();
      });
  }

  //Составное Future дает возможность реагировать на исход нескольких вариантов будущего.
  @Test
  void future_composition(Vertx vertx, VertxTestContext context) {
    // Создаем несколько обещаний (Promise)
    var one = Promise.<Void>promise();
    var two = Promise.<Void>promise();
    var three = Promise.<Void>promise();

    // Future
    var futureOne = one.future();
    var futureTwo = two.future();
    var futureThree = three.future();

    //Композиция может быть представвлена несколькими методами: all, any, join
    Future.all(futureOne, futureTwo, futureThree)
      .onFailure(context::failNow)
      .onSuccess(result -> {
        LOG.debug("Success!");
        context.completeNow();
      });

    // Complete futures
    vertx.setTimer(500, id -> {
      one.complete();
      two.complete();
      three.complete();
    });

  }
}
