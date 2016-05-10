package excilys.dashboardadministrator.ui.asyncs;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.ViewGroup;

import excilys.dashboardadministrator.ui.utils.AndroidUtils;

public class DecodeBitmapFromResourceAsync extends AsyncTask<Void, Void, Bitmap> {
    private final String mUrl;
    private int mWidth, mHeight;
    private DecodeBitmapFromResourceListener mListener;
    public DecodeBitmapFromResourceAsync(String url, ViewGroup layout, DecodeBitmapFromResourceListener listener) {
        this.mWidth = layout.getMeasuredWidth();
        this.mHeight = layout.getMeasuredHeight();
        this.mListener = listener;
        this.mUrl = url;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        return AndroidUtils.decodeSampledBitmapFromResource(mUrl, mWidth, mHeight);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mListener.OnBitmapDecoded(bitmap);

    }
    public interface DecodeBitmapFromResourceListener {
        void OnBitmapDecoded(Bitmap bitmap);
    }
}
