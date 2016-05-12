package excilys.dashboardadministrator.ui.displayables;

import android.content.Context;
import android.view.ViewGroup;

public interface Displayable {
    /**
     * Cette fonction permet à un objet AbstractDisplayable de s'afficher à l'interieur
     * du layout parent passé en paramètre.
     * @param context
     * @param layout
     */
    void display(Context context, ViewGroup layout);
}
