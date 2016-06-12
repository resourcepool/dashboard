package com.excilys.shoofleurs.dashboard.database;

import android.content.Context;

import com.excilys.shoofleurs.dashboard.database.dao.BundleDao;
import com.excilys.shoofleurs.dashboard.database.dao.impl.BundleDaoImpl;
import com.excilys.shoofleurs.dashboard.database.dao.MediaDao;
import com.excilys.shoofleurs.dashboard.database.dao.impl.MediaDaoImpl;
import com.excilys.shoofleurs.dashboard.database.serializers.BundleSerializer;
import com.excilys.shoofleurs.dashboard.database.serializers.MediaSerializer;
import com.excilys.shoofleurs.dashboard.model.entities.Bundle;
import com.excilys.shoofleurs.dashboard.model.entities.Media;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.paperdb.Paper;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class DatabaseManager {
    public static final String REVISION_KEY = "revision";
    private final EventBus mEventBus;
    private BundleDao mBundleDao;
    private MediaDao mMediaDao;

    public DatabaseManager(Context context, EventBus eventBus) {
        this.mEventBus = eventBus;

        Paper.init(context);
        Paper.addSerializer(Bundle.class, new BundleSerializer());
        Paper.addSerializer(Media.class, new MediaSerializer());

        mBundleDao = new BundleDaoImpl();
        mMediaDao = new MediaDaoImpl();
    }

    public Long readLocalRevision() {
        return Paper.book().read(REVISION_KEY);
    }

    public void saveLocalRevision(Long revision) {
        Paper.book().write(REVISION_KEY, revision);
    }

    public void saveBundles(List<Bundle> bundles) {
        mBundleDao.saveAll(bundles);
    }
}
