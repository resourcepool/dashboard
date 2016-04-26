package excilys.dashboardadministrator.ui.displayables;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;

/**
 * This interface allows to display every type of content (pictures, videos, pdf, websit etc.)
 * inside a layout.
 */
public abstract class AbstractDisplayable implements Displayable {
    /**
     * Url of the content.
     */
    protected String mUrl;


    public AbstractDisplayable(String url) {
        this.mUrl = url;

    }

    @Override
    public abstract void display(Context context, ViewGroup layout);

    /**
     * This method check if a view of type T already exist inside the layout,
     * return it in that case, or create it in other case.
     * @param layout
     * @param context
     * @param valueType
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    protected  <T extends View> T addOrReplaceViewByType(ViewGroup layout, Context context, Class<? extends T> valueType) {
        T t = null;

        /* If the layout contains more than one view, we remove all of them */
        if (layout.getChildCount() > 1) {
            layout.removeAllViews();
        }

        /* If the layout contains one view, we check if it is of type T*/
        if (layout.getChildCount() == 1) {
            if (layout.getChildAt(0).getClass().equals(valueType)) {
                t = (T) layout.getChildAt(0);
            }

            else {
                layout.removeViewAt(0);
                try {
                    t = valueType.getDeclaredConstructor(Context.class).newInstance(context);
                    addViewToLayout(t, layout);
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        /* No views has been yet assigned to the layout, we create it */
        else {
            try {
                t = valueType.getDeclaredConstructor(Context.class).newInstance(context);
                addViewToLayout(t, layout);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return t;
    }



    /**
     * Add the view to the layout with the layout params for matching layout size
     * @param view
     * @param layout
     */
    private void addViewToLayout(View view, ViewGroup layout) {
        ViewGroup.LayoutParams params =  new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        layout.addView(view);
    }
}
