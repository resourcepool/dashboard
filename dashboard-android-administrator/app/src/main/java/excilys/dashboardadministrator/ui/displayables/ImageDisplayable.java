package excilys.dashboardadministrator.ui.displayables;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import excilys.dashboardadministrator.rest.VolleySingleton;
import excilys.dashboardadministrator.ui.asyncs.DecodeBitmapFromResourceAsync;
import excilys.dashboardadministrator.ui.utils.AndroidUtils;

public class ImageDisplayable extends AbstractDisplayable {
    private boolean waitForNoImmediate;

    public ImageDisplayable(String url) {
        super(url);
    }

    @Override
    public void display(final Context context, ViewGroup layout) {
        final ImageView imageView = addOrReplaceViewByType(layout, context, ImageView.class);
        imageView.setImageBitmap(null);
        if (mUrl == null) {
            Log.e(ImageDisplayable.class.getSimpleName(), "display: url null");
            return;
        }
        if (mUrl.contains("http")){
            downloadImage(context, imageView);
        }
        else {
            decodeBitmap(context, imageView, layout);
        }
    }

    private void downloadImage(Context context, final ImageView imageView) {
        VolleySingleton.getInstance(context).getImageLoader().get(mUrl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                /* isImmediate = true is the response from the cache
                 * The bitmap will be null the first time */
                if (isImmediate) {
                    if (response.getBitmap() == null) {
                        waitForNoImmediate = true;
                    }
                    else{
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }

                /* If this is the first time, we wait for the request response (when isImmediate = false)*/
                else {
                    if (waitForNoImmediate) {
                        imageView.setImageBitmap(response.getBitmap());
                        waitForNoImmediate = false;
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(getClass().getSimpleName(), "onErrorResponse :"+error);
            }
        });
    }

    private void decodeBitmap(final Context context, final ImageView imageView, ViewGroup layout) {
        Bitmap bitmap = null;
        if ((bitmap = VolleySingleton.getInstance(context).getBitmapInCache(mUrl)) != null) {
            imageView.setImageBitmap(bitmap);
            Log.i(ImageDisplayable.class.getSimpleName(), "display: Load from Cache");
        }

        else {
            Log.i(ImageDisplayable.class.getSimpleName(), "display: Load Local File");
            new DecodeBitmapFromResourceAsync(mUrl, layout, new DecodeBitmapFromResourceAsync.DecodeBitmapFromResourceListener() {
                @Override
                public void OnBitmapDecoded(Bitmap bitmap) {
                    imageView.setImageBitmap(bitmap);
                    VolleySingleton.getInstance(context).putBitmapInCache(mUrl, bitmap);
                }
            }).execute();
        }
    }
}

