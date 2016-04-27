package excilys.dashboardadministrator.ui.displayables;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class WebDisplayable extends AbstractDisplayable {
    private WebView mWebView;

    public WebDisplayable(String url) {
        super(url);
    }

    @Override
    public void display(Context context, ViewGroup layout) {
//        mWebView = addOrReplaceViewByType(layout, context, WebView.class);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.loadUrl(mUrl);
    }
}
