package excilys.dashboardadministrator.ui.displayables;

import java.util.ArrayList;
import java.util.List;

import excilys.dashboardadministrator.model.entities.AbstractContent;
import excilys.dashboardadministrator.model.entities.ImageContent;
import excilys.dashboardadministrator.model.entities.VideoContent;
import excilys.dashboardadministrator.model.entities.WebContent;

public class DisplayableFactory {
    public static AbstractDisplayable create(AbstractContent abstractContent) {
        if (abstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) abstractContent;
            return new ImageDisplayable(imageContent.getUrl());
        }

        else if (abstractContent instanceof VideoContent) {
            VideoContent videoContent = (VideoContent) abstractContent;
            return new VideoDisplayable(videoContent.getUrl());
        }

        else if (abstractContent instanceof WebContent) {
            WebContent webContent = (WebContent) abstractContent;
                return new WebDisplayable(webContent.getUrl());
        }

        throw new IllegalArgumentException("This AbstractContent ("+abstractContent.toString()+") has not DisplayableFactory");
    }

    public static List<AbstractDisplayable> createAll(List<AbstractContent> abstractContents) {
        List<AbstractDisplayable> displayables = new ArrayList<>();
        for (AbstractContent abstractContent : abstractContents) {
            displayables.add(create(abstractContent));
        }
        return displayables;
    }
}
