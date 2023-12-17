package org.dshid.vertex_starter.promise;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dshid.vertex_starter.eventloops.EventLoopExampleMain;

public class PromiseExample extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(EventLoopExampleMain.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());

    //Promise используется для записи конечного значения (write)

    final Promise<Void> returningVoid = Promise.promise();
    returningVoid.complete();

    final Promise<String> returningString = Promise.promise();
    vertx.setTimer(500, id ->
      returningString.complete("Success!"));

    final Promise<JsonObject> returningJsonObject = Promise.promise();
    returningJsonObject.complete(new JsonObject().put("message", "Hello!"));

    //Future используется для чтения значения из Promise (read)
    //и поддерживает два хэндлера - на успех и на эксепшен

    final Future<String> future = returningString.future();
    future
      .onSuccess(
        result -> {
          LOG.info("Future result {}", result);
        }
      )
      .onFailure(e -> {
        LOG.error("Throw: ", e);
      });

    startPromise.complete();
  }
}
