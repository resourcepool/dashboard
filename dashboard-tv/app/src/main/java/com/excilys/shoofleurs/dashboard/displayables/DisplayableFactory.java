package com.excilys.shoofleurs.dashboard.displayables;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;
import com.excilys.shoofleurs.dashboard.model.entities.ImageContent;
import com.excilys.shoofleurs.dashboard.model.entities.VideoContent;

/**
 * Created by excilys on 20/04/16.
 */
public class DisplayableFactory {

    public static AbstractDisplayable create(AbstractContent pAbstractContent) {
        if (pAbstractContent instanceof ImageContent) {
            ImageContent imageContent = (ImageContent) pAbstractContent;
            return new ImageDisplayable(imageContent.getUrl(), imageContent.getDurationInDiaporama());
        }

        else if (pAbstractContent instanceof VideoContent) {
            VideoContent videoContent = (VideoContent) pAbstractContent;
            /**TODO trouver un moyen de définir le temps d'un video*/
            return new VideoDisplayable(videoContent.getUrl(), 100000);
//            return new VideoDisplayable(videoContent.getUrl(), AbstractDisplayable.INDETERMINED_TIME);
        }

        throw new IllegalArgumentException("This AbstractContent ("+pAbstractContent.toString()+") ha snot DisplayableFactory");
    }
}
