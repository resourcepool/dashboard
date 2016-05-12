package com.excilys.shoofleurs.dashboard.ui.displayables;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;

import com.excilys.shoofleurs.dashboard.ui.displayables.Displayable;

import java.lang.reflect.InvocationTargetException;

/**
 * This interface allows to display every type of content (pictures, videos, pdf, websit etc.)
 * inside a layout.
 */
public abstract class AbstractDisplayable implements Displayable {
    /**
     * Url of the content to display.
     */
    protected String mUrl;

    /**
     * The duration of the content display.
     */

    /**
     * The duration of the content display.
     */
    protected int mDurationInSec;

    /**
     * The listener to handle the completion
     * of the content.
     */

    /**
     * The listener to handle the completion
     * of the content.
     */
    protected OnCompletionListener mCompletionListener;

    /**
     * To handle completion with a delay.
     */

    /**
     * To handle completion with a delay.
     */
    private Handler mHandler;

    /**
     * To prevent the completion listener
     */
    protected boolean mStopDisplay;


    public AbstractDisplayable(String url, OnCompletionListener listener) {
        this.mUrl = url;
        this.mCompletionListener = listener;
        mHandler = new Handler();

    }

    public AbstractDisplayable(String url, int duration, OnCompletionListener listener) {
        this.mUrl = url;
        this.mDurationInSec = duration;
        this.mCompletionListener = listener;
        mHandler = new Handler();
    }

    @Override
    public void stop() {
        mStopDisplay = true;
    }

    @Override
    public abstract void display(Context context, ViewGroup layout);

    @Override
    public abstract void start();

    /**
     * This method check if a view of type T already exist inside the layout,
     * return it in that case, or create it in other case.
     * @param layout
     * @param context
     * @param valueType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected  <T extends View> T addOrReplaceViewByType(ViewGroup layout, Context context, Class<? extends T> valueType) {
        T t = null;

        /* If the layout contains more than one view, we remove all of them */
        if (layout.getChildCount() > 1) {
            layout.removeAllViews();
        }

        /* If the layout contains one view, we check if it is of type T*/
        if (layout.getChildCount() == 1) {
            if (layout.getChildAt(0).getClass().equals(valueType)) {
                t = (T) layout.getChildAt(0);
            }

            else {
                layout.removeViewAt(0);
                try {
                    t = valueType.getDeclaredConstructor(Context.class).newInstance(context);
                    addViewToLayout(t, layout);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        /* No views has been yet assigned to the layout, we create it */
        else {
            try {
                t = valueType.getDeclaredConstructor(Context.class).newInstance(context);
                addViewToLayout(t, layout);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return t;
    }



    /**
     * Add the view to the layout with the layout params for matching layout size
     * @param view
     * @param layout
     */
    private void addViewToLayout(View view, ViewGroup layout) {
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        layout.addView(view);
    }

    /**
     * Handle the onDisplayableCompletion method when the duration delay is reached.
     */
    protected void handleDelayedCompletion() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handleCompletion();
            }
        }, mDurationInSec*1000);
    }

    /**
     * Handle the onDisplayableCompletion method of the listener.
     */
    protected void handleCompletion() {
        if (mCompletionListener != null && !mStopDisplay) {
            mCompletionListener.onDisplayableCompletion();
        }
    }

    public void setOnCompletionListener(OnCompletionListener completionListener) {
        this.mCompletionListener = completionListener;
    }

    public interface OnCompletionListener {
        void onDisplayableCompletion();
    }
}
