package com.excilys.shoofleurs.dashboard.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.excilys.shoofleurs.dashboard.ui.controllers.SlideShowController;
import com.excilys.shoofleurs.dashboard.ui.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.ui.fragments.DisplayableFragment;
import com.excilys.shoofleurs.dashboard.ui.utils.LoopingPagerAdapter;

import java.util.List;

public class SlideShowPagerAdapter extends LoopingPagerAdapter<AbstractDisplayable> implements AbstractDisplayable.OnCompletionListener {
    private List<AbstractDisplayable> mDisplayables;
    private SlideShowController mController;
    private int mCurrentPage;

    public SlideShowPagerAdapter(FragmentManager fragmentManager, SlideShowController controller, List<AbstractDisplayable> displayables) {
        super(fragmentManager, displayables);
        this.mDisplayables = displayables;
        this.mController = controller;
    }

    @Override
    public Fragment getRealItem(int position) {
        AbstractDisplayable displayable = mDisplayables.get(position);
        return DisplayableFragment.newInstance(displayable);
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
        mController.nextPage();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        if (positionOffset == 0) {
//            mController.getDashboardActivity().showBackground();
//        } else {
//            mController.getDashboardActivity().hideBackground();
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public Displayable getCurrentDisplayable() {
        return mDisplayables.get(mCurrentPage);
    }
}
