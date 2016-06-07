package com.excilys.shooflers.dashboard.server.dao.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * LocalDateTimeSerializer for Jackson. We are not using the JSR310 serializer provided by Jackson
 * beacause we prefer reprenting the date by a string rather than a object.
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializer) throws IOException {
        jsonGenerator.writeString(dateTime.toString());
    }
}
