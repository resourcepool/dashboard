package com.excilys.shoofleurs.dashboard.displayables;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.model.entities.VideoContent;

public class DisplayableFactory {


    public static AbstractDisplayable create(AbstractContent pAbstractContent) {
        if (pAbstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) pAbstractContent;
            return new ImageDisplayable(imageContent.getUrl(), imageContent.getDurationInDiaporama());
        }

        else if (pAbstractContent instanceof VideoContent) {
            VideoContent videoContent = (VideoContent) pAbstractContent;
            return new VideoDisplayable(videoContent.getUrl());
        }

        throw new IllegalArgumentException("This AbstractContent ("+pAbstractContent.toString()+") has not DisplayableFactory");
    }



    public static AbstractDisplayable create(AbstractContent pAbstractContent, AbstractDisplayable.OnCompletionListener listener) {
        if (pAbstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) pAbstractContent;
            return new ImageDisplayable(imageContent.getUrl(), imageContent.getDurationInDiaporama(), listener);
        }

        else if (pAbstractContent instanceof VideoContent) {
            VideoContent videoContent = (VideoContent) pAbstractContent;
            return new VideoDisplayable(videoContent.getUrl(), listener);
        }

        throw new IllegalArgumentException("This AbstractContent ("+pAbstractContent.toString()+") has not DisplayableFactory");
    }
}
