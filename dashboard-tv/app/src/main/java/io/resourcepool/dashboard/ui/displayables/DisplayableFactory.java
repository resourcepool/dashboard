package io.resourcepool.dashboard.ui.displayables;

import io.resourcepool.dashboard.BuildConfig;
import io.resourcepool.dashboard.model.entities.Media;

import java.util.ArrayList;
import java.util.List;

public class DisplayableFactory {
    public static AbstractDisplayable create(Media media, AbstractDisplayable.OnCompletionListener listener) {
        switch (media.getMediaType()) {
            case IMAGE_JPG:
                return new ImageDisplayable(BuildConfig.API_URL + media.getUrl(), 5, listener);
            case IMAGE_PNG:
                return new ImageDisplayable(BuildConfig.API_URL + media.getUrl(), 5, listener);
            case VIDEO_MP4:
                return new VideoDisplayable(BuildConfig.API_URL + media.getUrl(), listener);
            case VIDEO_MPEG:
                return new VideoDisplayable(BuildConfig.API_URL + media.getUrl(), listener);
            case WEB_SITE:
                return new WebDisplayable(media.getUrl(), 5, listener);
        }

        throw new IllegalArgumentException("This media (" + media.toString() + ") has not DisplayableFactory");
    }

    public static List<AbstractDisplayable> createAll(List<Media> medias, AbstractDisplayable.OnCompletionListener onCompletionListener) {
        List<AbstractDisplayable> displayables = new ArrayList<>();
        for (Media media : medias) {
            displayables.add(create(media, onCompletionListener));
        }
        return displayables;
    }
}
