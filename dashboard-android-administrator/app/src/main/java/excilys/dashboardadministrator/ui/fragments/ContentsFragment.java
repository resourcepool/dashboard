package excilys.dashboardadministrator.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.model.entities.SlideShow;
import excilys.dashboardadministrator.ui.adapters.ContentsAdapter;
import excilys.dashboardadministrator.ui.dialogs.ChooseContentDialog;
import excilys.dashboardadministrator.ui.displayables.Displayable;
import excilys.dashboardadministrator.ui.displayables.DisplayableFactory;
import excilys.dashboardadministrator.ui.displayables.ImageDisplayable;
import excilys.dashboardadministrator.ui.utils.FilePathUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContentsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentsFragment extends Fragment {
    private List<Displayable> mDisplayables;

    private OnContentsFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ContentsAdapter mAdapter;

    private FloatingActionButton mAddFloatingActionButton;

    private SlideShow mSlideShow;

    public ContentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentsFragment newInstance(SlideShow slideShow) {
        ContentsFragment fragment = new ContentsFragment();
        fragment.setSlideShow(slideShow);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contents, container, false);
        setUpAddFloatingActionButton();
        mDisplayables = new ArrayList<>();
        setUpRecyclerView(rootView);
        return rootView;
    }

    private void setUpAddFloatingActionButton() {
        mAddFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChooseContentDialog chooseContentDialog = new ChooseContentDialog();
                chooseContentDialog.show(getActivity().getSupportFragmentManager(), ChooseContentDialog.class.getSimpleName());
            }
        });
    }

    private void setUpRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.contents_recycler_view);

        // use a Grid layout manager
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (mSlideShow != null) {
            mDisplayables.addAll(DisplayableFactory.createAll(mSlideShow.getContents()));
        }

        mAdapter = new ContentsAdapter(mDisplayables);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(getClass().getSimpleName(), "onActivityResult: "+requestCode);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ChooseContentDialog.PICK_IMAGE_REQUEST) {
                String realPath = FilePathUtils.getPath(getContext(), data.getData());
                Log.i(getClass().getSimpleName(), "onActivityResult: PICK_IMAGE " + realPath);
                Log.i(getClass().getSimpleName(), "RealPath: "+realPath);
                mAdapter.addDisplayable(new ImageDisplayable(realPath));
            }

            else if (requestCode == ChooseContentDialog.PICK_VIDEO_REQUEST) {
                String realPath = FilePathUtils.getPath(getContext(), data.getData());
                Log.i(getClass().getSimpleName(), "onActivityResult: PICK_VIDEO " + realPath);

            }
        }
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onContentsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContentsFragmentInteractionListener) {
            mListener = (OnContentsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSlideShow(SlideShow mSlideShow) {
        this.mSlideShow = mSlideShow;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnContentsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onContentsFragmentInteraction(Uri uri);
    }
}
