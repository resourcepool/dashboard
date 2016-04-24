package com.excilys.shoofleurs.dashboard.displayables;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.model.entities.VideoContent;
import com.excilys.shoofleurs.dashboard.model.entities.WebContent;

public class DisplayableFactory {
    public static AbstractDisplayable create(AbstractContent pAbstractContent, AbstractDisplayable.OnCompletionListener listener) {
        if (pAbstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) pAbstractContent;
            return new ImageDisplayable(imageContent.getUrl(), imageContent.getDurationInSlideShow(), listener);
        }

        else if (pAbstractContent instanceof VideoContent) {
            VideoContent videoContent = (VideoContent) pAbstractContent;
            return new VideoDisplayable(videoContent.getUrl(), listener);
        }

        else if (pAbstractContent instanceof WebContent) {
            WebContent webContent = (WebContent) pAbstractContent;
                return new WebDisplayable(webContent.getUrl(), webContent.isAutoScroll() ? 0 : webContent.getDurationInSlideshow(), listener);
        }

        throw new IllegalArgumentException("This AbstractContent ("+pAbstractContent.toString()+") has not DisplayableFactory");
    }
}
