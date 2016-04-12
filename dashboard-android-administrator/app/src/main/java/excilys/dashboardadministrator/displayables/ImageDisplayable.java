package excilys.dashboardadministrator.displayables;

import android.content.Context;
import android.os.Parcel;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.toolbox.NetworkImageView;

import excilys.dashboardadministrator.singletons.VolleySingleton;

public class ImageDisplayable implements Displayable {
    private String mUrl;

    public ImageDisplayable(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public void displayContent(Context context, LinearLayout linearLayout) {
        NetworkImageView networkImageView = new NetworkImageView(context);
        networkImageView.setImageUrl(mUrl, VolleySingleton.getInstance(context).getImageLoader());
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        networkImageView.setAdjustViewBounds(true);
        networkImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        networkImageView.setLayoutParams(params);

        linearLayout.addView(networkImageView);

    }
}

