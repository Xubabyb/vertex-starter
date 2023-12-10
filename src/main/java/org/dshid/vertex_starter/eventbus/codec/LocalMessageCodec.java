package org.dshid.vertex_starter.eventbus.codec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class LocalMessageCodec<T> implements MessageCodec<T, T> {

  private final String typeName;

  public LocalMessageCodec(Class<T> type) {
    this.typeName = type.getName();
  }

  @Override
  public String name() {
    return this.typeName;
  }

  @Override
  public void encodeToWire(Buffer buffer, T t) {
    throw new UnsupportedOperationException("only local encode is supported");
  }

  @Override
  public T decodeFromWire(int pos, Buffer buffer) {
    throw new UnsupportedOperationException("only local decode is supported");
  }

  @Override
  public T transform(T t) {
    return t;
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}
