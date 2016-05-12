package com.excilys.shoofleurs.dashboard.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;


public class DisplayableFragment extends Fragment {
    /**
     * The layout encompassing the contents
     */
    private RelativeLayout mContentLayout;

    private Displayable mDisplayable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_displayable, container, false);
        mContentLayout = (RelativeLayout) rootView.findViewById(R.id.current_content_layout);
        return rootView;
    }

    public static DisplayableFragment newInstance(Displayable displayable) {
        DisplayableFragment fragment = new DisplayableFragment();
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