package io.resourcepool.dashboard.rest.service;

import android.content.Context;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import io.resourcepool.dashboard.database.DashboardPrefs;
import io.resourcepool.dashboard.database.DatabaseController;
import io.resourcepool.dashboard.database.dao.BundleDao;
import io.resourcepool.dashboard.database.dao.MediaDao;
import io.resourcepool.dashboard.rest.dtos.ChangesetWrapper;
import io.resourcepool.dashboard.rest.dtos.Revision;
import io.resourcepool.dashboard.rest.events.GetDummyResponseEvent;
import io.resourcepool.dashboard.rest.events.HasUpdateResponseEvent;
import io.resourcepool.dashboard.rest.events.LoadDiffResponseEvent;
import io.resourcepool.dashboard.rest.service.api.DiffApi;
import io.resourcepool.dashboard.ui.DashboardApplication;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class performs all discovery requests to the server
 */
public class DiffService {

    private static final String TAG = "DiscoveryService";
    private DiffApi mDiscoveryApi;
    private EventBus mEventBus;
    private Context mCtx;
    private DatabaseController mDatabaseController;
    private BundleDao mBundleDao;
    private MediaDao mMediaDao;
    private final BundleService mBundleService;
    private boolean initialized;

    public DiffService(DashboardApplication ctx, EventBus eventBus) {
        this.mCtx = ctx;
        this.mDatabaseController = ctx.getDatabaseController();
        this.mBundleDao = mDatabaseController.getBundleDao();
        this.mMediaDao = mDatabaseController.getMediaDao();
        this.mBundleService = ctx.getBundleService();
        this.mEventBus = eventBus;

    }

    public void initialize(String baseUrl) {
        this.mDiscoveryApi = ServiceGenerator.createService(baseUrl, DiffApi.class);
        this.initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void hasUpdate() {
        if (!initialized) {
            throw new IllegalStateException("Service must be initialized before calling its methods");
        }
        final long revision = DashboardPrefs.getRevision(mCtx);
        Call<Long> call = mDiscoveryApi.getLatestRevision();
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(new HasUpdateResponseEvent(revision, response.body() > revision));
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code() + ": " + response.message());
                    mEventBus.post(new HasUpdateResponseEvent(revision, false));
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                mEventBus.post(new HasUpdateResponseEvent(revision, false));
            }
        });
    }

    public void loadDiff() {
        if (!initialized) {
            throw new IllegalStateException("Service must be initialized before calling its methods");
        }
        final long revision = DashboardPrefs.getRevision(mCtx);
        Call<ChangesetWrapper> call = mDiscoveryApi.loadDiff(revision, DashboardPrefs.getFeed(mCtx));
        call.enqueue(new Callback<ChangesetWrapper>() {
            @Override
            public void onResponse(Call<ChangesetWrapper> call, Response<ChangesetWrapper> response) {
                if (response.isSuccessful()) {
                    mEventBus.post(new LoadDiffResponseEvent(response.body()));
                } else {
                    Log.e(TAG, "onResponse: Error " + response.code() + ": " + response.message());
                    mEventBus.post(new LoadDiffResponseEvent(null));
                }
            }

            @Override
            public void onFailure(Call<ChangesetWrapper> call, Throwable t) {
                mEventBus.post(new LoadDiffResponseEvent(null));
            }
        });
    }

    public void handleDiffs(ChangesetWrapper changeset) {
        for (Revision rev : changeset.getChanges()) {
            switch (rev.getAction()) {
                case DELETE:
                    switch (rev.getType()) {
                        case BUNDLE:
                            mBundleDao.delete(rev.getTarget());
                            break;
                        case MEDIA:
                            mMediaDao.delete(rev.getTarget());
                            break;
                        default:
                            mEventBus.post(new GetDummyResponseEvent());
                            break;
                    }
                    break;
                case UPDATE:
                    switch (rev.getType()) {
                        case BUNDLE:
                            mBundleDao.delete(rev.getTarget());
                            mBundleService.fetchBundle(rev.getResult());
                            break;
                        case MEDIA:
                            mMediaDao.delete(rev.getTarget());
                            mBundleService.fetchMedia(rev.getResult());
                            break;
                        default:
                            mEventBus.post(new GetDummyResponseEvent());
                            break;
                    }
                    break;
                case ADD:
                    switch (rev.getType()) {
                        case BUNDLE:
                            mBundleService.fetchBundle(rev.getTarget());
                            break;
                        case MEDIA:
                            mBundleService.fetchMedia(rev.getTarget());
                            break;
                        default:
                            mEventBus.post(new GetDummyResponseEvent());
                            break;
                    }
                    break;
            }
        }
    }

}
