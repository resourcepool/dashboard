package excilys.dashboardadministrator.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.model.entities.ImageContent;
import excilys.dashboardadministrator.model.entities.SlideShow;
import excilys.dashboardadministrator.model.json.ServerResponse;
import excilys.dashboardadministrator.rest.ContentApi;
import excilys.dashboardadministrator.rest.ServiceGenerator;
import excilys.dashboardadministrator.ui.adapters.ContentsAdapter;
import excilys.dashboardadministrator.ui.dialogs.ChooseContentDialog;
import excilys.dashboardadministrator.ui.displayables.Displayable;
import excilys.dashboardadministrator.ui.displayables.DisplayableFactory;
import excilys.dashboardadministrator.ui.displayables.ImageDisplayable;
import excilys.dashboardadministrator.ui.utils.FilePathUtils;
import excilys.dashboardadministrator.utils.JsonMapperUtils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @BindView(R.id.contents_recycler_view)
    RecyclerView mRecyclerView;

    FloatingActionButton mAddFloatingActionButton;

    private RecyclerView.LayoutManager mLayoutManager;
    private ContentsAdapter mAdapter;

    private SlideShow mSlideShow;

    private ContentApi mContentApi;

    public ContentsFragment() {
        mContentApi = ServiceGenerator.createService(ContentApi.class);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContentsFragment.
     */
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
        ButterKnife.bind(this, rootView);

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

                ImageContent imageContent = new ImageContent("Default");
                imageContent.setGlobalDuration(0);
                imageContent.setDurationInSlideShow(10);
                imageContent.setSlideShow(mSlideShow);
                imageContent.setPositionInSlideShow(mSlideShow.getContents().size());

                File image = new File(realPath);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), image);
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", image.getName(), requestFile);

                Call<ServerResponse> call = mContentApi.postContents(JsonMapperUtils.writeValueAsString(imageContent), body);
                call.enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        ImageContent imageContent1 = JsonMapperUtils.getServerResponseContent(response.body(), ImageContent.class);
                        mAdapter.addDisplayable(new ImageDisplayable(imageContent1.getUrl()));
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) { }
                });
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

    public interface OnContentsFragmentInteractionListener {
        void onContentsFragmentInteraction(Uri uri);
    }
}
