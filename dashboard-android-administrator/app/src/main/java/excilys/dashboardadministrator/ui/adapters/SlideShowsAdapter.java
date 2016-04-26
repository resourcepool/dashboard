package excilys.dashboardadministrator.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.model.entities.SlideShow;
import excilys.dashboardadministrator.ui.displayables.Displayable;
import excilys.dashboardadministrator.ui.displayables.DisplayableFactory;

public class SlideShowsAdapter extends RecyclerView.Adapter<SlideShowsAdapter.ViewHolder> {
    private List<SlideShow> mSlideShows;
    private Context mContext;
    private View.OnClickListener mOnClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private RelativeLayout mSlideShowContentLayout;
        private TextView mSlideShowTitleTewtView;
        private TextView mSlideShowInfosTewtView;
        public ViewHolder(View rootView) {
            super(rootView);
            mSlideShowContentLayout = (RelativeLayout) rootView.findViewById(R.id.item_slideshow_container);
            mSlideShowTitleTewtView = (TextView) rootView.findViewById(R.id.item_slideshow_title);
            mSlideShowInfosTewtView = (TextView) rootView.findViewById(R.id.item_slideshow_infos);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SlideShowsAdapter(List<SlideShow> myDataset) {
        mSlideShows = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SlideShowsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slideshow, parent, false);
        mContext = parent.getContext();
        // set the view's size, margins, paddings and layout parameters
        ViewHolder viewHolder = new ViewHolder(rootView);
        if (mOnClickListener != null) {
            rootView.setOnClickListener(mOnClickListener);
        }
        return viewHolder;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SlideShow currentSlideShow = mSlideShows.get(position);
        holder.mSlideShowTitleTewtView.setText(currentSlideShow.getTitle());
        holder.mSlideShowInfosTewtView.setText(currentSlideShow.getContents().size() + " " + "contents");

        if (mSlideShows.get(position).getContents().size() > 0) {
            Displayable displayable = DisplayableFactory.create(mSlideShows.get(position).getContents().get(0));
            displayable.display(mContext, holder.mSlideShowContentLayout);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mSlideShows.size();
    }

    public SlideShow getItem(int position) {
        return mSlideShows.get(position);
    }

    public void addSlideShow(SlideShow slideShow) {
        mSlideShows.add(slideShow);
        notifyDataSetChanged();
    }

    public void addSlideShows(List<SlideShow> slideShows) {
        mSlideShows.addAll(slideShows);
        notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }
}
