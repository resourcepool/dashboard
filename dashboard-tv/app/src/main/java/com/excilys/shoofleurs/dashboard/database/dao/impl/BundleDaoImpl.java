package com.excilys.shoofleurs.dashboard.database.dao.impl;

import android.util.Log;

import com.excilys.shoofleurs.dashboard.database.dao.BundleDao;
import com.excilys.shoofleurs.dashboard.model.entities.Bundle;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Book;
import io.paperdb.Paper;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class BundleDaoImpl implements BundleDao {
    private static final String TAG = "BundleDaoImpl";
    private Book bundleBook = Paper.book(BundleDao.ENTITY_NAME);
    Gson mGson = new Gson();

    @Override
    public Bundle get(String uuid) {
        return bundleBook.read(uuid);
    }

    @Override
    public List<Bundle> getAll() {
        List<Bundle> bundles = new ArrayList<>();
        for (String key : bundleBook.getAllKeys()) {
            bundles.add((Bundle) bundleBook.read(key));
        }
        return bundles;
    }

    @Override
    public void save(Bundle bundle) {
        Log.i(TAG, "save: ");
        if (bundleBook.exist(bundle.getUuid())) {
            Log.i(TAG, "save: " + bundleBook.read(bundle.getUuid()));
        }
        bundleBook.write(bundle.getUuid(), bundle);
    }

    public void save(List<Bundle> bundles) {
        if (bundles != null) {
            for (Bundle bundle : bundles) {
                save(bundle);
            }
        }
    }

    @Override
    public void delete(String uuid) {
        bundleBook.delete(uuid);
    }
}
