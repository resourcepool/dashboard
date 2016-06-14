package com.excilys.shooflers.dashboard.server.util;

import java.text.Normalizer;

/**
 * @author Loïc Ortola on 13/06/2016.
 */
public class StringUtils {

    private static final String NON_ASCII_CHARACTERS_PATTERN = "[^\\w\\s]";

    /**
     * Normalizes a string.
     *
     * @param str the string to normalize
     * @return the normalized string
     */
    public static String normalize(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFKD).replaceAll("_", " ");
        str = org.apache.commons.lang3.StringUtils.normalizeSpace(str).replaceAll("\\s+", "_");
        str = str.replaceAll(NON_ASCII_CHARACTERS_PATTERN, "").toLowerCase();
        return str;
    }
}
