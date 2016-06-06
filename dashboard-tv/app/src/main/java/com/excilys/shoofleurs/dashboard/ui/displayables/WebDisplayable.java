package com.excilys.shoofleurs.dashboard.ui.displayables;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;


public class WebDisplayable extends AbstractDisplayable {
    private WebView mWebView;
    private Handler mHandler;
    private boolean onScrollCompleted;
    private Runnable mScrollRunnable;

    public WebDisplayable(String url, int duration, OnCompletionListener listener) {
        super(url, duration, listener);
        mHandler = new Handler();
    }

    @Override
    public void display(Context context, ViewGroup layout) {
        mWebView = addOrReplaceViewByType(layout, context, WebView.class);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
    }

    @Override
    public void start() {
        mStopDisplay = false;
        onScrollCompleted = false;
        if (mDurationInSec == 0) {
            handleAutoscroll(mWebView);
        } else {
            handleDelayedCompletion();
        }
    }


    private void handleAutoscroll(final WebView webView) {
        Log.i(WebDisplayable.class.getSimpleName(), "handleAutoscroll: ");
        mScrollRunnable = new Runnable() {
            @Override
            public void run() {
                webView.scrollBy(0, 1);
                int contentHeight = (int) (webView.getContentHeight() * webView.getScale());
                if (contentHeight - webView.getScrollY() <= webView.getHeight()) {
                    if (mCompletionListener != null && !onScrollCompleted) {
                        mCompletionListener.onDisplayableCompletion();
                        onScrollCompleted = true;
                        Log.i(WebDisplayable.class.getSimpleName(), "handleAutoscroll: onCompleted");
                    }
                } else {
                /* Restart the current thread after 20 ms*/
                    mHandler.postDelayed(this, 20);
                }
            }
        };

            /* Wait 5 seconds before start autoscrolling*/
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(WebDisplayable.class.getSimpleName(), "handleAutoScroll: start autoScroll");
                mScrollRunnable.run();
            }
        }, 5000);
    }
}
