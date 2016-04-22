package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.excilys.shoofleurs.dashboard.utils.VideoCacheProxyManager;

public class VideoDisplayable extends AbstractDisplayable{
    public VideoDisplayable(String url) {
        super(url);
    }

    public VideoDisplayable(String url, OnCompletionListener listener) {
        super(url, listener);
    }

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mCompletionListener != null) {
                mCompletionListener.onCompletion();
            }
        }
    };


    @Override
    public void displayContent(Context context, ViewGroup layout) {
        /* Proxy Url for caching */
        HttpProxyCacheServer proxy = VideoCacheProxyManager.getProxy(context);
        String proxyUrl = proxy.getProxyUrl(mUrl);

        VideoView videoView = addOrReplaceViewByType(layout, context, VideoView.class);
        //Use a media controller so that you can scroll the video contents
        //and also to pause, start the video.
        MediaController mediaController = new MediaController(context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(proxyUrl);
        videoView.setOnCompletionListener(onCompletionListener);
        videoView.start();
    }
}
