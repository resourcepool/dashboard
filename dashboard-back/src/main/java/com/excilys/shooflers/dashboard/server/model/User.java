package com.excilys.shooflers.dashboard.server.model;

/**
 * This class represents a user.
 *
 * @author Loïc Ortola on 07/06/2016.
 */
public class User {

  private String login;

  private String password;

  /**
   * Default constructor.
   */
  public User() {
  }

  /*=========================================*/
  /*--------- GETTERS AND SETTERS -----------*/
  /*=========================================*/

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return login.equals(user.login);
  }

  @Override
  public int hashCode() {
    return login.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("User{");
    sb.append(", login='").append(login);
    sb.append(", password='").append(password);
    sb.append('}');
    return sb.toString();
  }

  /*=========================================*/
  /*------------BUILDER----------------------*/
  /*=========================================*/

  /* CHECKSTYLE_OFF: JAVADOC */

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {
    private final User user;

    public Builder() {
      user = new User();
    }

    public Builder login(String login) {
      user.setLogin(login);
      return this;
    }

    public Builder password(String password) {
      user.setPassword(password);
      return this;
    }

    public User build() {
      return user;
    }
  }
}