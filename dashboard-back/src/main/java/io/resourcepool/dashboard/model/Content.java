package io.resourcepool.dashboard.model;

import io.resourcepool.dashboard.model.type.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Loïc Ortola on 14/06/2016.
 */
public class Content {
    private InputStream inputStream;
    private String contentType;
    private String extension;

    public Content(MultipartFile m, MediaType mediaType) {
        try {
            this.inputStream = m.getInputStream();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        this.contentType = m.getContentType();
        this.extension = mediaType.getExtension(contentType);
    }

    public Content(InputStream is, String mimeType, String extension) {
        this.contentType = mimeType;
        this.inputStream = is;
        this.extension = extension;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public String getExtension() {
        return extension;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
