package io.resourcepool.dashboard.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import io.resourcepool.dashboard.ui.displayables.AbstractDisplayable;
import io.resourcepool.dashboard.ui.displayables.Displayable;
import io.resourcepool.dashboard.ui.fragments.MediaFragment;
import io.resourcepool.dashboard.ui.utils.LoopingPagerAdapter;
import io.resourcepool.dashboard.ui.views.DashboardView;

import java.util.List;

public class MediaPagerAdapter extends LoopingPagerAdapter<AbstractDisplayable> implements AbstractDisplayable.OnCompletionListener {
    private List<AbstractDisplayable> mDisplayables;
    private DashboardView mDashboardView;
    private int mCurrentPage;

    public MediaPagerAdapter(FragmentManager fragmentManager, DashboardView dashboardView, List<AbstractDisplayable> displayables) {
        super(fragmentManager, displayables);
        this.mDisplayables = displayables;
        this.mDashboardView = dashboardView;
    }

    @Override
    public Fragment getRealItem(int position) {
        AbstractDisplayable displayable = mDisplayables.get(position);
        return MediaFragment.newInstance(displayable);
    }

    @Override
    public void onRealPageSelected(int position) {
        if (mDisplayables.isEmpty()) {
            return;
        }
        mDisplayables.get(position).start();
        mCurrentPage = position;
    }

    @Override
    public void onDisplayableCompletion() {
        if (mDisplayables.size() != 1) {
            mDashboardView.nextMedia();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public Displayable getCurrentDisplayable() {
        return mDisplayables.get(mCurrentPage);
    }
}
