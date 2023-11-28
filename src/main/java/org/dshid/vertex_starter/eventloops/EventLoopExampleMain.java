package org.dshid.vertex_starter.eventloops;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class EventLoopExampleMain extends AbstractVerticle {

  private static final Logger LOG = LogManager.getLogger(EventLoopExampleMain.class);

  public static void main(String[] args) {
    var vertx = Vertx.vertx(
      // Вертекс опции позволяют настраивать процесс выполнения
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500) //максимальное время выполнения
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1) //интервал вызова проверок блокировки
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
        .setEventLoopPoolSize(2) //Можем регулировать размер пула event-loop потоков
    );
    vertx.deployVerticle(EventLoopExampleMain.class.getName(),
/*          Развернув 4 истанса на размер пула в 2 потока,
       получим в логах следующую картину:
      - Запуск двух вертиклов,
      - Срабатывание блок чекеров на запуск блокирующей операции
      - Разворачивание 2-ух оставшихся инстансов
*/
      new DeploymentOptions().setInstances(4));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start verticle - {}", getClass().getSimpleName());
    startPromise.complete();
    //don't do this in production
    // Вертикл задеплоен, внутри мы вызываем блокирующую операцию,
    // 'усыпляем' поток выполнения на время более чем значение по умолчанию (2000мс) время срабатывания проверки блокировки).
    // Thread.sleep(5000);
    Thread.sleep(5000);
    // в логах мы увидим предупреждение о том, что выполнение  было заблокировано
    // Thread Thread[vert.x-eventloop-thread-0,5,main] has been blocked for 2708 ms, time limit is 2000 ms

  }
}
