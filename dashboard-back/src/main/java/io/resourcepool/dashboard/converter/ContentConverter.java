package io.resourcepool.dashboard.converter;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Loïc Ortola on 14/06/2016.
 */
public interface ContentConverter {

    /**
     * @return the output extension with dot of a converted asset (ex: .pdf)
     */
    String getOutputExtension();

    /**
     * @param mimeType the mime type (application/json is a valid example)
     * @return true if supported, false otherwise
     */
    boolean supports(String mimeType);

    /**
     * @param extension the extension (with dot. ex: .pdf)
     * @return true if supported, false otherwise
     */
    boolean supportsExtension(String extension);

    /**
     * Perform content conversion
     *
     * @param inputStream  the original input stream
     * @param outputStream the destination stream
     */
    void convert(InputStream inputStream, OutputStream outputStream);
}
