package com.excilys.shoofleurs.dashboard.ui.displayables;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.model.entities.VideoContent;
import com.excilys.shoofleurs.dashboard.model.entities.WebContent;

import java.util.ArrayList;
import java.util.List;

public class DisplayableFactory {
    public static AbstractDisplayable create(AbstractContent abstractContent, AbstractDisplayable.OnCompletionListener listener) {
        if (abstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) abstractContent;
            return new ImageDisplayable(imageContent.getUrl(), imageContent.getDurationInSlideShow(), listener);
        } else if (abstractContent instanceof VideoContent) {
            VideoContent videoContent = (VideoContent) abstractContent;
            return new VideoDisplayable(videoContent.getUrl(), listener);
        } else if (abstractContent instanceof WebContent) {
            WebContent webContent = (WebContent) abstractContent;
            return new WebDisplayable(webContent.getUrl(), webContent.getIsAutoScroll() ? 0 : webContent.getDurationInSlideshow(), listener);
        }

        throw new IllegalArgumentException("This AbstractContent (" + abstractContent.toString() + ") has not DisplayableFactory");
    }

    public static List<AbstractDisplayable> createAll(List<AbstractContent> abstractContents, AbstractDisplayable.OnCompletionListener onCompletionListener) {
        List<AbstractDisplayable> displayables = new ArrayList<>();
        for (AbstractContent abstractContent : abstractContents) {
            displayables.add(create(abstractContent, onCompletionListener));
        }
        return displayables;
    }
}
