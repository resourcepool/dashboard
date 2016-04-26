package excilys.dashboardadministrator.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.model.entities.SlideShow;
import excilys.dashboardadministrator.service.SlideShowService;
import excilys.dashboardadministrator.ui.activities.MainActivity;
import excilys.dashboardadministrator.ui.adapters.SlideShowsAdapter;
import excilys.dashboardadministrator.ui.listener.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnSlideShowsFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SlideShowsFragment extends Fragment {
    private List<SlideShow> mSlideShows;

    private OnSlideShowsFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SlideShowsAdapter mAdapter;

    private FloatingActionButton mAddFloatingActionButton;

    private MainActivity mMainActivity;

    public SlideShowsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_slideshows, container, false);
        mMainActivity = (MainActivity) getActivity();
        setUpAddFloatingActionButton();
        mSlideShows = new ArrayList<>();
        setUpRecyclerView(rootView);
        updateSlideShows();

        return rootView;
    }

    private void updateSlideShows() {
        SlideShowService.getInstance(getActivity()).checkUpdates(new SlideShowService.OnSlideShowServiceResponse() {
            @Override
            public void onCheckUpdatesResponse(List<SlideShow> slideShows) {
                mAdapter.addSlideShows(slideShows);
            }
        });
    }

    private void setUpAddFloatingActionButton() {
        mAddFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.add_fab);
        mAddFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ChooseContentDialog chooseContentDialog = new ChooseContentDialog();
//                chooseContentDialog.show(getActivity().getSupportFragmentManager(), ChooseContentDialog.class.getSimpleName());
            }
        });
    }

    private void setUpRecyclerView(View rootView) {
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.slidewhows_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new SlideShowsAdapter(mSlideShows);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mMainActivity.baskStackAndReplaceFragment(ContentsFragment.newInstance(mAdapter.getItem(position)), true);
            }
        }));
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onSlideShowsFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSlideShowsFragmentInteractionListener) {
            mListener = (OnSlideShowsFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnSlideShowsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSlideShowsFragmentInteraction(Uri uri);
    }
}
