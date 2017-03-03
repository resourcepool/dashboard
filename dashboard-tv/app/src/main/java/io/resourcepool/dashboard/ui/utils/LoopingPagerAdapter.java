package io.resourcepool.dashboard.ui.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

public abstract class LoopingPagerAdapter<T> extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
    protected List<T> mDataset;

    private LoopingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public LoopingPagerAdapter(FragmentManager fm, List<T> dataset) {
        this(fm);
        this.mDataset = dataset;
    }

    @Override
    public final Fragment getItem(int position) {
        return getRealItem(getRealPosition(position));
    }

    @Override
    public final int getCount() {
        return (mDataset == null || mDataset.isEmpty()) ? 0 : Integer.MAX_VALUE;
    }

    public abstract Fragment getRealItem(int position);

    public void addData(T t) {
        if (mDataset != null) {
            mDataset.add(t);
            notifyDataSetChanged();
        }
    }

    public void addAllDatas(List<T> ts) {
        if (mDataset != null) {
            mDataset.addAll(ts);
            notifyDataSetChanged();
        }
    }

    public void clearAllDatas() {
        if (mDataset != null) {
            mDataset.clear();
            notifyDataSetChanged();
        }
    }

    private int getRealPosition(int position) {
        return mDataset == null ? 0 : position % mDataset.size();
    }

    @Override
    public final void onPageSelected(int position) {
        onRealPageSelected(getRealPosition(position));
    }

    public void onRealPageSelected(int position) { }
}
