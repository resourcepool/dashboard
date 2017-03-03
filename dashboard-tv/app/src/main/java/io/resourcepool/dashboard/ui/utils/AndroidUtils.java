package io.resourcepool.dashboard.ui.utils;

import android.animation.Animator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import io.resourcepool.dashboard.ui.activities.DashboardActivity;

public class AndroidUtils {
    public static void hideStatusBar(DashboardActivity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void setVisibility(int visibility, View... views) {
        for (View v : views) {
            v.setVisibility(visibility);
        }
    }

    public static void cancelAnimators(Animator... animators) {
        for (Animator a : animators) {
            if (a != null) {
                a.cancel();
            }
        }
    }

    public static void startAnimators(Animator... animators) {
        for (Animator a : animators) {
            if (a != null) {
                a.start();
            }
        }
    }
}
