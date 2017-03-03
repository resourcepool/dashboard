package io.resourcepool.dashboard.database.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.resourcepool.dashboard.model.entities.Media;
import com.google.gson.Gson;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class MediaSerializer extends Serializer<Media> {
    private Gson mGson = new Gson();

    @Override
    public void write(Kryo kryo, Output output, Media object) {
        output.writeString(mGson.toJson(object));
    }

    @Override
    public Media read(Kryo kryo, Input input, Class<Media> type) {
        return mGson.fromJson(input.readString(), Media.class);
    }
}
