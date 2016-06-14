package com.excilys.shooflers.dashboard.server.model.type;

import com.excilys.shooflers.dashboard.server.converter.ContentConverter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Loïc Ortola on 14/06/2016.
 */
public class MediaTypeDescriptor {

    private List<ContentConverter> converters = new LinkedList<>();
    private Map<String, String> mimeTypes = new HashMap<>();

    public List<ContentConverter> getConverters() {
        return converters;
    }

    public Map<String, String> getMimeTypes() {
        return mimeTypes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        MediaTypeDescriptor d = new MediaTypeDescriptor();

        private Builder() {

        }

        public Builder converter(ContentConverter converter) {
            d.converters.add(converter);
            return this;
        }

        public Builder mimeType(String mimeType, String extension) {
            d.mimeTypes.put(mimeType, extension);
            return this;
        }

        public MediaTypeDescriptor build() {
            return d;
        }
    }


}
