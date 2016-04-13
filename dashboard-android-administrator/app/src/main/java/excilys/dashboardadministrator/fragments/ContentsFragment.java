package excilys.dashboardadministrator.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.adapters.ContentsAdapter;
import excilys.dashboardadministrator.dialogs.ChooseContentDialog;
import excilys.dashboardadministrator.displayables.Displayable;
import excilys.dashboardadministrator.displayables.ImageDisplayable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnContentsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Displayable> mDisplayables;

    private OnContentsFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private FloatingActionButton mAddFloatingActionButton;

    public ContentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentsFragment newInstance(String param1, String param2) {
        ContentsFragment fragment = new ContentsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);


        // specify an adapter (see also next example)
        Displayable[] displayables = new Displayable[10];
        displayables[0] = new ImageDisplayable("https://pbs.twimg.com/profile_images/578893796676612096/biGGRXHl.png");
        displayables[1] = new ImageDisplayable("https://pbs.twimg.com/profile_images/510058837374550017/VmbQIxTG_400x400.png");
        displayables[2] = new ImageDisplayable("https://avatars3.githubusercontent.com/u/14311089?v=3&s=200");
        displayables[3] = new ImageDisplayable("https://pbs.twimg.com/profile_images/510058837374550017/VmbQIxTG_400x400.png");
        displayables[4] = new ImageDisplayable("https://avatars3.githubusercontent.com/u/14311089?v=3&s=200");
        displayables[5] = new ImageDisplayable("https://pbs.twimg.com/profile_images/510058837374550017/VmbQIxTG_400x400.png");
        displayables[6] = new ImageDisplayable("https://pbs.twimg.com/profile_images/578893796676612096/biGGRXHl.png");
        displayables[7] = new ImageDisplayable("https://avatars3.githubusercontent.com/u/14311089?v=3&s=200");
        displayables[8] = new ImageDisplayable("https://pbs.twimg.com/profile_images/578893796676612096/biGGRXHl.png");
        displayables[9] = new ImageDisplayable("https://pbs.twimg.com/profile_images/510058837374550017/VmbQIxTG_400x400.png");

        mDisplayables.addAll(Arrays.asList(displayables));

        mAdapter = new ContentsAdapter(mDisplayables);
        mRecyclerView.setAdapter(mAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
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
