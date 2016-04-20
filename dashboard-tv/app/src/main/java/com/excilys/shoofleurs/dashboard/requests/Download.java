package com.excilys.shoofleurs.dashboard.requests;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.net.URL;


/**
 * Télécharge une image depuis une URL.
 */
public class Download extends AsyncTask<Void, Void, Bitmap> {
    private int mCode;

    private ICallback mCallback;

    private String mUrl;


    public Download(int code, ICallback callback, String url) {
        mCode = code;
        mCallback = callback;
        mUrl = url;
    }


    @Override
    protected Bitmap doInBackground(Void... params) {
        try {
            URL url = new URL(mUrl);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        mCallback.asyncTaskFinish(bitmap, mCode);
    }
}
