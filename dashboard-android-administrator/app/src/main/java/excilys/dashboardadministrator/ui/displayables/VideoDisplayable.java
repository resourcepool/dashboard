package excilys.dashboardadministrator.ui.displayables;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;

import excilys.dashboardadministrator.ui.managers.VideoCacheProxyManager;

public class VideoDisplayable extends AbstractDisplayable {
    private VideoView mVideoView;

    public VideoDisplayable(String url) {
        super(url);
    }

    @Override
    public void display(Context context, ViewGroup layout) {
        /* Proxy Url for caching */
        HttpProxyCacheServer proxy = VideoCacheProxyManager.getProxy(context);
        String proxyUrl = proxy.getProxyUrl(mUrl);

        mVideoView = addOrReplaceViewByType(layout, context, VideoView.class);
        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(mVideoView);
        mVideoView.setMediaController(mediaController);
        mVideoView.setVideoPath(proxyUrl);
        mVideoView.start();
    }
}
