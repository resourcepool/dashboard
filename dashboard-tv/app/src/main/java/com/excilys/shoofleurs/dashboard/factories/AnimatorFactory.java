package com.excilys.shoofleurs.dashboard.factories;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class AnimatorFactory {
    public static AnimatorSet createBackTranslateAnimatorSet (View view){
        Animator translationX = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, 0f);
        Animator translationY = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, translationY);
        animatorSet.setDuration(200);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        return animatorSet;
    }

    public static Animator createFadeInAnimator (final View view){
        Animator fadeAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f);
        fadeAnimator.setDuration(200);
        fadeAnimator.setInterpolator(new AccelerateInterpolator());

        fadeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        return fadeAnimator;
    }

    public static Animator createFadeOutAnimator (final View view){
        Animator fadeAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1f, 0f);
        fadeAnimator.setDuration(200);
        fadeAnimator.setInterpolator(new AccelerateInterpolator());

        fadeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        return fadeAnimator;
    }

    public static AnimatorSet createRightToLeftExitAnimatorSet (View ... views){
        int startDelay = 0;
        int difDelay = 100;
        List<Animator> animators = new ArrayList<>();
        for (View view:views){
            float xTranslate;
            if (view.getX() >= 0)
                xTranslate = view.getX()+view.getWidth()+100;
            else xTranslate = view.getWidth()+100;
            Log.i(AnimatorFactory.class.getSimpleName(), "translate : "+startDelay+" :"+xTranslate);
            Animator translateAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, view.getTranslationX(), -xTranslate);
            translateAnimator.setStartDelay(startDelay);
            translateAnimator.setDuration(200);
            translateAnimator.setInterpolator(new AccelerateInterpolator());
            animators.add(translateAnimator);
            startDelay+=difDelay;
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);

        return animatorSet;
    }

    public static AnimatorSet createLeftToRighEnterAnimatorSet (View ... views){
        List<Animator> animators = new ArrayList<>();
        for (View view:views){
            view.setTranslationY(0);
            float xTranslate = ((RelativeLayout)view.getParent()).getWidth()+view.getWidth()+100;
            Animator translateAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, xTranslate, 0);
            translateAnimator.setDuration(200);
            translateAnimator.setInterpolator(new DecelerateInterpolator());
            animators.add(translateAnimator);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);

        return animatorSet;
    }


    public static Animator createDownToUpTranslationAnimator (View view, int height){
        Animator translateAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, height, 0);
        translateAnimator.setDuration(100);
        translateAnimator.setInterpolator(new DecelerateInterpolator());
        return translateAnimator;
    }

    public static Animator createUpToDownTranslationAnimator (View view, int height){
        Animator translateAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0, height);
        translateAnimator.setDuration(100);
        translateAnimator.setInterpolator(new AccelerateInterpolator());
        return translateAnimator;
    }

    public static AnimatorSet createVibrationAnimatorSet(View view){
        float initialAngle = view.getRotation();
        Animator vibrationAnimator1 = ObjectAnimator.ofFloat(view, View.ROTATION, initialAngle, initialAngle + 10);
        vibrationAnimator1.setDuration(50);
        Animator vibrationAnimator2 = ObjectAnimator.ofFloat(view, View.ROTATION, initialAngle + 10, initialAngle -10);
        vibrationAnimator2.setDuration(100);
        Animator vibrationAnimator3 = ObjectAnimator.ofFloat(view, View.ROTATION, initialAngle -10, initialAngle);
        vibrationAnimator3.setDuration(50);

        AnimatorSet animatorSet  = new AnimatorSet();
        animatorSet.playSequentially(vibrationAnimator1, vibrationAnimator2, vibrationAnimator3);

        return animatorSet;
    }

    public static AnimatorSet createZoomInAndOutAnimatorSet (View view){
        long duration = 60;
        Animator zoomInAnimatorX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 1.2f);
        zoomInAnimatorX.setDuration(duration);
        Animator zoomInAnimatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 1.2f);
        zoomInAnimatorY.setDuration(duration);
        Animator zoomOutAnimatorX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1.2f, 1f);
        zoomOutAnimatorX.setDuration(duration);
        Animator zoomOutAnimatorY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1.2f, 1f);
        zoomOutAnimatorY.setDuration(duration);

        AnimatorSet zoomInAnimatorSet = new AnimatorSet();
        zoomInAnimatorSet.playTogether(zoomInAnimatorX, zoomInAnimatorY);

        AnimatorSet zoomOutAnimatorSet = new AnimatorSet();
        zoomOutAnimatorSet.playSequentially(zoomOutAnimatorX, zoomOutAnimatorY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(zoomInAnimatorSet, zoomOutAnimatorSet);

        return animatorSet;
    }

    public static AnimatorSet createTranslationToAnimatorSet (View view, float toX, float toY){
        Animator translateXAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, toX);
        Animator translateYAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, toY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translateXAnimator, translateYAnimator);
        animatorSet.setDuration(100);
        return animatorSet;
    }

    public static AnimatorSet createActionBarRectanglesBackInAnimator(View rec0, View rec1, View rec2){
        ObjectAnimator animRotate0 = ObjectAnimator.ofFloat(rec0, View.ROTATION, -45);
        ObjectAnimator animRotate2 = ObjectAnimator.ofFloat(rec2, View.ROTATION, 45);

        ObjectAnimator animTranslateX0 = ObjectAnimator.ofFloat(rec0, View.TRANSLATION_X, -rec1.getWidth()/4);
        ObjectAnimator animTranslateX2 = ObjectAnimator.ofFloat(rec2, View.TRANSLATION_X, -rec1.getWidth()/4);

        ObjectAnimator animScaleX0 = ObjectAnimator.ofFloat(rec0, View.SCALE_X, 0.5f);
        ObjectAnimator animScaleX1 = ObjectAnimator.ofFloat(rec1, View.SCALE_X, 0.9f);
        ObjectAnimator animScaleX2 = ObjectAnimator.ofFloat(rec2, View.SCALE_X, 0.5f);

        ObjectAnimator animTranslateY0 = ObjectAnimator.ofFloat(rec0, View.TRANSLATION_Y, rec1.getHeight()+rec1.getHeight()/2);
        ObjectAnimator animTranslateY2 = ObjectAnimator.ofFloat(rec2, View.TRANSLATION_Y, -rec1.getHeight()-rec1.getHeight()/2);

        AnimatorSet animatorSetGroup = new AnimatorSet();
        animatorSetGroup.setInterpolator(new DecelerateInterpolator());
        animatorSetGroup.playTogether(animTranslateX0, animTranslateX2, animScaleX0, animScaleX2, animScaleX1, animTranslateY0, animTranslateY2, animRotate0, animRotate2);
        return animatorSetGroup;
    }

    public static AnimatorSet createActionBarRectanglesBackOutAnimator(View rec0, View rec1, View rec2){
        ObjectAnimator animRotate0 = ObjectAnimator.ofFloat(rec0, View.ROTATION, 0);
        ObjectAnimator animRotate2 = ObjectAnimator.ofFloat(rec2, View.ROTATION, 0);

        ObjectAnimator animTranslateX0 = ObjectAnimator.ofFloat(rec0, View.TRANSLATION_X, 0);
        ObjectAnimator animTranslateX2 = ObjectAnimator.ofFloat(rec2, View.TRANSLATION_X, 0);

        ObjectAnimator animScaleX0 = ObjectAnimator.ofFloat(rec0, View.SCALE_X, 1f);
        ObjectAnimator animScaleX1 = ObjectAnimator.ofFloat(rec1, View.SCALE_X, 1f);
        ObjectAnimator animScaleX2 = ObjectAnimator.ofFloat(rec2, View.SCALE_X, 1f);

        ObjectAnimator animTranslateY0 = ObjectAnimator.ofFloat(rec0, View.TRANSLATION_Y, 0);
        ObjectAnimator animTranslateY2 = ObjectAnimator.ofFloat(rec2, View.TRANSLATION_Y, 0);

        AnimatorSet animatorSetGroup = new AnimatorSet();
        animatorSetGroup.setInterpolator(new DecelerateInterpolator());
        animatorSetGroup.playTogether(animTranslateX0, animTranslateX2, animScaleX0, animScaleX2, animScaleX1, animTranslateY0, animTranslateY2, animRotate0, animRotate2);
        return animatorSetGroup;
    }


    public static AnimatorSet createProgressRoundAnimatorSet (View progressRound1, View progressRound2){
        ObjectAnimator rotateAnimator1 = ObjectAnimator.ofFloat(progressRound1, View.ROTATION, 0, 720);
        ObjectAnimator rotateAnimator2 = ObjectAnimator.ofFloat(progressRound2, View.ROTATION, 720, 0);

        rotateAnimator1.setInterpolator(new LinearInterpolator());
        rotateAnimator2.setInterpolator(new LinearInterpolator());


        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(rotateAnimator1, rotateAnimator2);
        animatorSet.setDuration(40000);
        AnimatorListenerAdapter animationListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        };

        animatorSet.addListener(animationListener);

        return animatorSet;
    }


    public static AnimatorSet createProgressPointAnimatorSet(View point, int size){
        int transitionDuration = 300;
        float scale = size*0.045f;
        int pointSize = point.getLayoutParams().width;
        //1
        Animator translateAnimator1 = ObjectAnimator.ofFloat(point, View.TRANSLATION_Y, 0, size-pointSize);
        translateAnimator1.setDuration(transitionDuration);

        Animator scaleAnimator11 = ObjectAnimator.ofFloat(point, View.SCALE_Y, 1, scale);
        scaleAnimator11.setDuration(transitionDuration / 2);
        Animator scaleAnimator12 = ObjectAnimator.ofFloat(point, View.SCALE_Y, scale, 1);
        scaleAnimator12.setDuration(transitionDuration / 2);

        AnimatorSet scaleAnimatorSet1 = new AnimatorSet();
        scaleAnimatorSet1.playSequentially(scaleAnimator11, scaleAnimator12);

        AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playTogether(translateAnimator1, scaleAnimatorSet1);

        //2
        Animator translateAnimator2 = ObjectAnimator.ofFloat(point, View.TRANSLATION_X, 0, size-pointSize);
        translateAnimator2.setDuration(transitionDuration);

        Animator scaleAnimator21 = ObjectAnimator.ofFloat(point, View.SCALE_X, 1, scale);
        scaleAnimator21.setDuration(transitionDuration / 2);
        Animator scaleAnimator22 = ObjectAnimator.ofFloat(point, View.SCALE_X, scale, 1);
        scaleAnimator22.setDuration(transitionDuration / 2);

        AnimatorSet scaleAnimatorSet2 = new AnimatorSet();
        scaleAnimatorSet2.playSequentially(scaleAnimator21, scaleAnimator22);

        AnimatorSet animatorSet2 = new AnimatorSet();
        animatorSet2.playTogether(translateAnimator2, scaleAnimatorSet2);

        //3
        Animator translateAnimator3 = ObjectAnimator.ofFloat(point, View.TRANSLATION_Y, size-pointSize, 0);
        translateAnimator3.setDuration(transitionDuration);

        /*Animator scaleAnimator21 = ObjectAnimator.ofFloat(point, View.SCALE_X, 1, scale);
        scaleAnimator21.setDuration(transitionDuration / 2);
        Animator scaleAnimator22 = ObjectAnimator.ofFloat(point, View.SCALE_X, scale, 1);
        scaleAnimator22.setDuration(transitionDuration / 2);*/

        AnimatorSet scaleAnimatorSet3 = new AnimatorSet();
        scaleAnimatorSet3.playSequentially(scaleAnimator11, scaleAnimator12);

        AnimatorSet animatorSet3 = new AnimatorSet();
        animatorSet3.playTogether(translateAnimator3, scaleAnimatorSet3);


        //4
        Animator translateAnimator4 = ObjectAnimator.ofFloat(point, View.TRANSLATION_X, size-pointSize, 0);
        translateAnimator4.setDuration(transitionDuration);

        /*Animator scaleAnimator21 = ObjectAnimator.ofFloat(point, View.SCALE_X, 1, scale);
        scaleAnimator21.setDuration(transitionDuration / 2);
        Animator scaleAnimator22 = ObjectAnimator.ofFloat(point, View.SCALE_X, scale, 1);
        scaleAnimator22.setDuration(transitionDuration / 2);*/

        AnimatorSet scaleAnimatorSet4 = new AnimatorSet();
        scaleAnimatorSet4.playSequentially(scaleAnimator21, scaleAnimator22);

        AnimatorSet animatorSet4 = new AnimatorSet();
        animatorSet4.playTogether(translateAnimator4, scaleAnimatorSet4);

        final AnimatorSet finalAnimatorSet = new AnimatorSet();
        finalAnimatorSet.playSequentially(animatorSet1, animatorSet2, animatorSet3, animatorSet4);

        finalAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                finalAnimatorSet.setStartDelay(0);
                finalAnimatorSet.start();
            }
        });

        finalAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());

        return finalAnimatorSet;
    }

}
