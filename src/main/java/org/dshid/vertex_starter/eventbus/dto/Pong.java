package org.dshid.vertex_starter.eventbus.dto;

public class Pong {

  private Integer id;

  public Pong() {
    //Default constructor
  }

  public Pong(Integer id) {
    this.id = id;
  }

  public int getId() {
    return this.id;
  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }
}
