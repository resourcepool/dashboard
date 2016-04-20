package com.excilys.shoofleurs.dashboard.displayables;

import android.content.Context;
import android.view.ViewGroup;

/**
 * Created by buonomo on 29/03/16.
 */

/**
 * Cette interface permet d'afficher n'importe quel type de média (image, vidéo, pdf, etc.)
 * à l'interieur d'un layout.
 */
public interface Displayable  {
    /**
     * Cette fonction permet à un objet Displayable de s'afficher à l'interieur
     * du layout parent passé en paramètre.
     * @param context
     * @param layout
     */
    void  displayContent(Context context, ViewGroup layout);
}
