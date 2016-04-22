package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by excilys on 22/04/16.
 */
public interface Displayable {
    /**
     * Cette fonction permet à un objet AbstractDisplayable de s'afficher à l'interieur
     * du layout parent passé en paramètre.
     * @param context
     * @param layout
     */
    void displayContent(Context context, ViewGroup layout);
}
