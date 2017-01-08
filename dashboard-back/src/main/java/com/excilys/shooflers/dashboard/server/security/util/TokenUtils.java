package com.excilys.shooflers.dashboard.server.security.util;

import com.excilys.shooflers.dashboard.server.security.model.TokenMetaData;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * The Session token contains the following information:
 * attemptLogin:passwordhash:expirationdate
 * the passwordhash is computed using
 *
 * @author Loïc Ortola on 07/06/2016.
 */
public class TokenUtils {

    private static StringEncryptor stringEncryptor = new StringEncryptor();

    /**
     * Builds an unencrypted token with the user id and the timeout timestamp.
     *
     * @param login            the User attemptLogin
     * @param hash             a salted-password hash
     * @param timeoutInMinutes the timeout in minutes
     * @return unencrypted token with the user id and the timeout timestamp
     */
    public static String buildClearToken(String login, String hash, long timeoutInMinutes) {
        StringBuilder sb = new StringBuilder();
        sb.append(login)
                .append(":")
                .append(hash)
                .append(":")
                .append(LocalDateTime.now().plusMinutes(timeoutInMinutes).toInstant(ZoneOffset.UTC).toEpochMilli());
        return sb.toString();
    }

    /**
     * Builds an unencrypted token with the user id and the timeout timestamp.
     *
     * @param login            the User attemptLogin
     * @param timeoutInMinutes the timeout in minutes
     * @return unencrypted token with the user id and the timeout timestamp
     */
    public static String buildToken(String login, String hash, long timeoutInMinutes) {
        String unencrypted = buildClearToken(login, hash, timeoutInMinutes);
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
        if (base64.length < 2) {
            return null;
        }

        return stringEncryptor.decrypt(base64[1]);
    }

    /**
     * Retrieve user meta-data from decrypted token.
     *
     * @param decryptedToken the decrypted token
     * @return the user (attemptLogin only)
     */
    public static TokenMetaData parse(String decryptedToken) {
        String[] explodedToken = decryptedToken.split(":");
        return new TokenMetaData(explodedToken[0], explodedToken[1]);
    }
}
