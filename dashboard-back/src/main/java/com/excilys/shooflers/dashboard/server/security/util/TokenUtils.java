package com.excilys.shooflers.dashboard.server.security.util;

import com.excilys.shooflers.dashboard.server.model.User;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Loïc Ortola on 07/06/2016.
 */
public class TokenUtils {

  private static StringEncryptor stringEncryptor = new StringEncryptor();

  /**
   * Builds an unencrypted token with the user id and the timeout timestamp.
   *
   * @param login            the User login
   * @param timeoutInMinutes the timeout in minutes
   * @return unencrypted token with the user id and the timeout timestamp
   */
  public static String buildClearToken(String login, long timeoutInMinutes) {
    StringBuilder sb = new StringBuilder();
    sb.append(login)
      .append(":")
      .append(LocalDateTime.now().plusMinutes(timeoutInMinutes).toInstant(ZoneOffset.UTC).toEpochMilli());
    return sb.toString();
  }

  /**
   * Builds an unencrypted token with the user id and the timeout timestamp.
   *
   * @param login            the User login
   * @param timeoutInMinutes the timeout in minutes
   * @return unencrypted token with the user id and the timeout timestamp
   */
  public static String buildToken(String login, long timeoutInMinutes) {
    String unencrypted = buildClearToken(login, timeoutInMinutes);
    return stringEncryptor.encrypt(unencrypted);
  }

  /**
   * Retrieve and decrypt request token from request.
   *
   * @param request the http request
   * @return the decrypted token
   */
  public static String parse(HttpServletRequest request) {
    if (request == null) {
      return null;
    }

    String tokenHeader = request.getHeader("Authorization");

    if (tokenHeader == null || !tokenHeader.startsWith("Token")) {
      return null;
    }

    String[] base64 = tokenHeader.split(" ");
    return stringEncryptor.decrypt(new String(Base64.decodeBase64(base64[1])));
  }

  /**
   * Retrieve user meta-data from decrypted token.
   * @param decryptedToken the decrypted token
   * @return the user (login only)
   */
  public static User parse(String decryptedToken) {
    return User.builder()
      .login(decryptedToken.split(":")[0])
      .build();
  }
}
