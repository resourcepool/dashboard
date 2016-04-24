package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;


public class WebDisplayable extends AbstractDisplayable{
    private Handler mHandler;
    private boolean onScrollCompleted;
    private Runnable mScrollRunnable;

    public WebDisplayable(String url, int duration, OnCompletionListener listener) {
        super(url, duration, listener);
        mHandler = new Handler();
    }

    @Override
    public void displayContent(Context context, ViewGroup layout) {
        final WebView webView = addOrReplaceViewByType(layout, context, WebView.class);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(mUrl);
        Log.i(WebDisplayable.class.getSimpleName(), "displayContent: " + mDurationInSec);
        if (mDurationInSec == 0) {
            setAutoscroll(webView);
        }

        else {
            handleCompletion();
        }
    }

    private void setAutoscroll(final WebView webView) {
        mScrollRunnable = new Runnable() {
            @Override
            public void run() {
                webView.scrollBy(0, 1);
                int contentHeight = (int) (webView.getContentHeight()*webView.getScale());
                if (contentHeight - webView.getScrollY() <= webView.getHeight()) {
                    if (mCompletionListener != null && !onScrollCompleted) {
                        mCompletionListener.onCompletion();
                        onScrollCompleted = true;
                    }
                }
                /* Restart the current thread after 20 ms*/
                else {
                    mHandler.postDelayed(this, 20);
                }
            }
        };

        /* Wait 5 seconds before start autoscrolling*/
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollRunnable.run();
            }
        }, 5000);
    }
}
