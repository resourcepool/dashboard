package com.excilys.shoofleurs.dashboard.controllers;

import android.util.Log;
import android.widget.RelativeLayout;

import com.excilys.shoofleurs.dashboard.R;
import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;
import com.excilys.shoofleurs.dashboard.displayables.Displayable;
import com.excilys.shoofleurs.dashboard.displayables.DisplayableFactory;
import com.excilys.shoofleurs.dashboard.model.entities.Diaporama;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * This controller controls the diaporamas's display
 */
public class DiaporamaController {
    private static DiaporamaController S_INSTANCE;

    private DashboardActivity mDashboardActivity;

    /**
     * The layout encompassing the contents
     */
    private RelativeLayout mContentLayout;

    /**
     * The sorted diaporama queue
     */
    private Queue<Diaporama> mDiaporamaQueue;

    /**
     * The current diaporama to be displayed
     */
    private Diaporama mCurrentDiaporama;

    public static DiaporamaController getInstance(DashboardActivity dashboardActivity) {
        if (S_INSTANCE == null) S_INSTANCE = new DiaporamaController(dashboardActivity);
        return S_INSTANCE;
    }

    private DiaporamaController(DashboardActivity dashboardActivity) {
        this.mDashboardActivity = dashboardActivity;
        mContentLayout = (RelativeLayout) mDashboardActivity.findViewById(R.id.current_content_layout);
        mDiaporamaQueue = new PriorityQueue<>();
    }


    public void addAllDiaporamas(Diaporama... diaporamas) {
        Log.i(getClass().getSimpleName(), "addAllDiaporamas: " + Arrays.asList(diaporamas));
        for (Diaporama d : diaporamas) {
            mDiaporamaQueue.offer(d);
        }

        if (mCurrentDiaporama != null) {
            if (!mDiaporamaQueue.peek().equals(mCurrentDiaporama)) {
                replaceDiaporama(mDiaporamaQueue.poll());
            }
        } else {
            replaceDiaporama(mDiaporamaQueue.poll());
        }
    }

    /**
     * Replace or create the current diaporama
     *
     * @param newDiaporama
     */
    public void replaceDiaporama(Diaporama newDiaporama) {
        this.mCurrentDiaporama = newDiaporama;
        Log.i(DashboardActivity.class.getSimpleName(), "replaceDiaporama: " + mCurrentDiaporama);
        startDiaporama();
    }

    /**
     * Start the diaporama to display
     */
    public void startDiaporama() {
        Log.i(getClass().getSimpleName(), "startDiaporama");

        mDashboardActivity.stopProgressView();
        if (mCurrentDiaporama != null) {
            if (mCurrentDiaporama.getContents().size() > 0){
                Displayable displayable = DisplayableFactory.create(mCurrentDiaporama.getContents().get(0));
                displayable.displayContent(mDashboardActivity, mContentLayout);
            }
        }
    }

}
