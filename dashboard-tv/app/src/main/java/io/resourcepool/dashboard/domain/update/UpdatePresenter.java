package io.resourcepool.dashboard.domain.update;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.resourcepool.dashboard.database.dao.BundleDao;
import io.resourcepool.dashboard.database.dao.MediaDao;
import io.resourcepool.dashboard.rest.dtos.ChangesetWrapper;
import io.resourcepool.dashboard.rest.events.GetBundleResponseEvent;
import io.resourcepool.dashboard.rest.events.GetDummyResponseEvent;
import io.resourcepool.dashboard.rest.events.GetMediaResponseEvent;
import io.resourcepool.dashboard.rest.events.LoadDiffResponseEvent;
import io.resourcepool.dashboard.rest.service.BundleService;
import io.resourcepool.dashboard.rest.service.DiffService;
import io.resourcepool.dashboard.ui.DashboardApplication;
import io.resourcepool.dashboard.ui.presenters.AbstractPresenter;

/**
 * @author Tommy Buonomo on 10/06/16.
 */
public class UpdatePresenter extends AbstractPresenter<UpdateView> {
    private static final String TAG = "UpdatePresenter";

    private final EventBus mEventBus;
    private final BundleService mBundleService;
    private final DiffService mDiffService;
    private final BundleDao mBundleDao;
    private final MediaDao mMediaDao;
    private UpdateView mUpdateView;

    public UpdatePresenter(DashboardApplication dashboardApplication) {
        super(dashboardApplication);
        mEventBus = dashboardApplication.getEventBus();
        mBundleService = dashboardApplication.getBundleService();
        mDiffService = dashboardApplication.getDiffService();
        mBundleDao = dashboardApplication.getDatabaseController().getBundleDao();
        mMediaDao = dashboardApplication.getDatabaseController().getMediaDao();
    }

    @Override
    public void attachView(UpdateView view) {
        this.mUpdateView = view;
        mUpdateView.showWaitingAnimation(true);
        mDiffService.loadDiff();
    }

    @Override
    public void onPause() {
        mEventBus.unregister(this);
    }

    @Override
    public void onResume() {
        if (!mEventBus.isRegistered(this)) {
            mEventBus.register(this);
        }
    }

    /****************************************************
     * EVENTS
     ****************************************************/
    @Subscribe
    @SuppressWarnings("unused")
    public void onLoadDiffResponseEvent(LoadDiffResponseEvent event) {
        ChangesetWrapper changeset = event.getChangeset();
        if (changeset != null && changeset.getChanges() != null && !changeset.getChanges().isEmpty()) {
            mUpdateView.showWaitingAnimation(false);
            mUpdateView.showLoadingAnimation(true, changeset.getChanges().size());
            mDiffService.handleDiffs(changeset);
        } else {
            mUpdateView.launchDashboardActivity();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onGetMediaResponseEvent(GetMediaResponseEvent event) {
        mUpdateView.incProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onGetBundleResponseEvent(GetBundleResponseEvent event) {
        mUpdateView.incProgress();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    @SuppressWarnings("unused")
    public void onGetDummyResponseEvent(GetDummyResponseEvent event) {
        mUpdateView.incProgress();
    }


}
