package io.resourcepool.dashboard.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import io.resourcepool.dashboard.R;
import io.resourcepool.dashboard.ui.displayables.Displayable;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MediaFragment extends Fragment {
    /**
     * The layout encompassing the contents
     */
    @BindView(R.id.current_content_layout)
    RelativeLayout mContentLayout;

    private Displayable mDisplayable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_media, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public static MediaFragment newInstance(Displayable displayable) {
        MediaFragment fragment = new MediaFragment();
        fragment.mDisplayable = displayable;
        return fragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDisplayable.display(getContext(), mContentLayout);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}