package com.excilys.shoofleurs.dashboard.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.excilys.shoofleurs.dashboard.ui.controllers.SlideShowController;
import com.excilys.shoofleurs.dashboard.ui.displayables.AbstractDisplayable;
import com.excilys.shoofleurs.dashboard.ui.fragments.DisplayableFragment;

import java.util.List;

public class SlideShowPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener, AbstractDisplayable.OnCompletionListener {
    private List<AbstractDisplayable> mDisplayables;
    private SlideShowController mController;

    public SlideShowPagerAdapter(FragmentManager fragmentManager, SlideShowController controller, List<AbstractDisplayable> displayables) {
        super(fragmentManager);
        this.mDisplayables = displayables;
        this.mController = controller;
    }

    public void addContents(List<AbstractDisplayable> contents) {
        mDisplayables.addAll(contents);
        notifyDataSetChanged();
    }

    public void clearContents() {
        mDisplayables.clear();
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(final int position) {
        Log.i(SlideShowPagerAdapter.class.getSimpleName(), "getItem: "+position);
        AbstractDisplayable displayable = mDisplayables.get(position);
        return DisplayableFragment.newInstance(displayable);
    }

    @Override
    public int getCount() {
        return mDisplayables.size();
    }

    @Override
    public void onPageSelected(int position) {
        mDisplayables.get(position).start();
    }

    @Override
    public void onDisplayableCompletion() {
        mController.nextPage();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
}
