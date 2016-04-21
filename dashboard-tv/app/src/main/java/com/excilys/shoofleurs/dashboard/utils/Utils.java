package com.excilys.shoofleurs.dashboard.utils;

import android.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.excilys.shoofleurs.dashboard.activities.DashboardActivity;

public class Utils {
    public static void hideStatusBar(DashboardActivity activity) {
//        Log.i(Utils.class.getSimpleName(), "hideStatusBar");
//        View decorView = activity.getWindow().getDecorView();
//        // Hide the status bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
//        // Remember that you should never show the action bar if the
//        // status bar is hidden, so hide that too if necessary.
//        ActionBar actionBar = activity.getActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}
