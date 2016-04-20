package com.excilys.shoofleurs.dashboard.displayables;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;

/**
 * Created by excilys on 20/04/16.
 */
public class DisplayableFactory {

    public static Displayable create(AbstractContent pAbstractContent) {
        if (pAbstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) pAbstractContent;
            return new ImageDisplayable(imageContent.getUrl());
        }

        throw new IllegalArgumentException("This AbstractContent ("+pAbstractContent.toString()+") ha snot DisplayableFactory");
    }
}
