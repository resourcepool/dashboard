package io.resourcepool.dashboard.model.type;

import io.resourcepool.dashboard.converter.ContentConverter;
import io.resourcepool.dashboard.converter.PowerpointToPdfConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Type of a media.
 */
public enum MediaType {

    IMAGE(MediaTypeDescriptor.builder()
            .mimeType("image/png", ".png")
            .mimeType("image/jpeg", ".jpeg")
            .mimeType("image/jpg", ".jpg")
            .mimeType("image/gif", ".gif")
            .build()),
    VIDEO(MediaTypeDescriptor.builder()
            .mimeType("video/mpeg", ".mpg")
            .mimeType("video/mp4", ".mp4")
            .mimeType("video/x-msvideo", ".avi")
            .mimeType("video/x-flv", ".flv")
            .build()),
    DOCUMENT(MediaTypeDescriptor.builder()
            .mimeType("application/pdf", ".pdf")
            .converter(new PowerpointToPdfConverter())
            .build()),
    WEB(MediaTypeDescriptor.builder().build()),
    NEWS(MediaTypeDescriptor.builder().build());

    private Map<String, String> mimeTypes;
    private List<ContentConverter> converters;

    MediaType(MediaTypeDescriptor d) {
        this.converters = d.getConverters();
        this.mimeTypes = d.getMimeTypes();
    }

    /**
     * @param mimeType the mime type (image/png is a valid input)
     * @return true if mime type is supported by media type, false otherwise
     */
    public boolean supports(String mimeType) {
        return mimeTypes.containsKey(mimeType) || converters.stream().filter(contentConverter -> contentConverter.supports(mimeType)).count() > 0;
    }

    /**
     * @param mimeType the mime type (image/png is a valid input)
     * @return true if file should be converted, false otherwise
     */
    public boolean shouldConvert(String mimeType) {
        return converters.stream().filter(contentConverter -> contentConverter.supports(mimeType)).count() > 0;
    }

    /**
     * @param mimeType the mime type (image/png is a valid input)
     * @return the target converter or null if no conversion is needed
     */
    public ContentConverter getConverter(String mimeType) {
        return converters.stream().filter(contentConverter -> contentConverter.supports(mimeType)).findFirst().orElseGet(() -> null);
    }

    /**
     * @param ext the extension (with or without a prefixed dot (.png or png are both valid inputs)
     * @return true if extension is supported by media type, false otherwise
     */
    public boolean supportsExtension(String ext) {
        final String dotExt = ext.startsWith(".") ? ext : "." + ext;
        return mimeTypes.containsValue(dotExt) || converters.stream().filter(contentConverter -> contentConverter.supportsExtension(dotExt)).count() > 0;
    }

    /**
     * MimeType to MediaType
     *
     * @param mimeType mime type to convert
     * @return MediaType
     */
    public static MediaType parseMimeType(String mimeType) {
        if (mimeType == null) {
            return null;
        }

        for (MediaType m : MediaType.values()) {
            if (m.supports(mimeType)) {
                return m;
            }
        }
        return null;
    }

    /**
     * @param mimeType the mime type
     * @return the valid extension for the mime type of current media
     */
    public String getExtension(String mimeType) {
        String ext = mimeTypes.get(mimeType);
        if (ext == null) {
            ext = converters.stream().filter(contentConverter -> contentConverter.supports(mimeType)).findFirst().get().getOutputExtension();
        }
        return ext;
    }

    public Collection<String> getValidMimeTypes() {
        return mimeTypes.keySet();
    }
}