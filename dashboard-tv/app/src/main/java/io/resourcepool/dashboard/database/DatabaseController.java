package io.resourcepool.dashboard.database;

import android.content.Context;

import io.resourcepool.dashboard.database.dao.BundleDao;
import io.resourcepool.dashboard.database.dao.impl.BundleDaoImpl;
import io.resourcepool.dashboard.database.dao.MediaDao;
import io.resourcepool.dashboard.database.dao.impl.MediaDaoImpl;
import io.resourcepool.dashboard.database.serializers.BundleSerializer;
import io.resourcepool.dashboard.database.serializers.MediaSerializer;
import io.resourcepool.dashboard.model.entities.Bundle;
import io.resourcepool.dashboard.model.entities.Media;

import org.greenrobot.eventbus.EventBus;

import io.paperdb.Paper;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class DatabaseController {
    private final EventBus mEventBus;
    private BundleDao mBundleDao;
    private MediaDao mMediaDao;

    public DatabaseController(Context context, EventBus eventBus) {
        this.mEventBus = eventBus;
        //mEventBus.register(this);

        Paper.init(context);
        Paper.addSerializer(Bundle.class, new BundleSerializer());
        Paper.addSerializer(Media.class, new MediaSerializer());

        mBundleDao = new BundleDaoImpl();
        mMediaDao = new MediaDaoImpl();
    }
}
