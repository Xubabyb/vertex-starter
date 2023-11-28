package org.dshid.vertex_starter.scaling;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ScalingExampleMain extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(ScalingExampleMain.class);

  // Количество разворачиваемых инстансов,
  // каждый инстанс запускается в своем потоке (если количество ядер процессора ссответствует)
  private static final Integer NUMBER = 6;
  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ScalingExampleMain());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());

    //деплой по имени вертикла , вторым параметром указываются опции деплоя
    vertx.deployVerticle(VerticleN.class.getName(),
      new DeploymentOptions()
        .setInstances(NUMBER)
        .setConfig(new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", VerticleN.class.getSimpleName()))
    );
    startPromise.complete();
  }
}
