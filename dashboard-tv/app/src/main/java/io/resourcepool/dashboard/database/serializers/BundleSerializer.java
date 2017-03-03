package io.resourcepool.dashboard.database.serializers;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import io.resourcepool.dashboard.model.entities.Bundle;
import com.google.gson.Gson;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class BundleSerializer extends Serializer<Bundle> {
    private Gson mGson = new Gson();

    @Override
    public void write(Kryo kryo, Output output, Bundle object) {
        output.writeString(mGson.toJson(object));
    }

    @Override
    public Bundle read(Kryo kryo, Input input, Class<Bundle> type) {
        return mGson.fromJson(input.readString(), Bundle.class);
    }
}
