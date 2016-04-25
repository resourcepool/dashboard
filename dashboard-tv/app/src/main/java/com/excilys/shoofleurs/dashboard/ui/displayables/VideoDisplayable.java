package com.excilys.shoofleurs.dashboard.ui.displayables;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.excilys.shoofleurs.dashboard.ui.managers.VideoCacheProxyManager;

public class VideoDisplayable extends AbstractDisplayable {
    private VideoView mVideoView;

    public VideoDisplayable(String url, OnCompletionListener listener) {
        super(url, listener);
    }

    private MediaPlayer.OnCompletionListener onMediaCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (mCompletionListener != null) {
                mCompletionListener.onDisplayableCompletion();
            }

            if (mVideoView != null) {
                mVideoView.stopPlayback();
            }
        }
    };

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
        mVideoView.setOnCompletionListener(onMediaCompletionListener);
    }

    @Override
    public void start() {
        mVideoView.start();
    }
}
