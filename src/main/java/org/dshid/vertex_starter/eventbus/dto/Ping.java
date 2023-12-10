package org.dshid.vertex_starter.eventbus.dto;

public class Ping {

  private String message;
  private boolean enabled;

  public Ping() {
    //Default constructor
  }

  public Ping(String message, boolean enabled) {
    this.message = message;
    this.enabled = enabled;
  }

  public String getMessage() {
    return message;
  }

  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public String toString() {
    return "Ping{" +
      "message='" + message + '\'' +
      ", enabled=" + enabled +
      '}';
  }
}
