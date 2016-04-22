package com.excilys.shoofleurs.dashboard.controllers;

import android.view.ViewGroup;

import com.excilys.shoofleurs.dashboard.model.entities.AbstractContent;

/**
 * Created by excilys on 21/04/16.
 */
public class ContentController {
    private static ContentController INSTANCE = null;
    
    public static ContentController getInstance() {
        if (INSTANCE == null) INSTANCE = new ContentController();
        return INSTANCE;
    }

    private ContentController() {

    }

    public void startContent(AbstractContent content, ViewGroup group) {
//        AbstractDisplayable displayable = DisplayableFactory.create(content);
//        displayable.displayContent();
    }


}
