package excilys.dashboardadministrator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import excilys.dashboardadministrator.R;
import excilys.dashboardadministrator.displayables.Displayable;

/**
 * Created by buonomo on 29/03/16.
 */
public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.ViewHolder> {
        private List<Displayable> mDataset;
        private Context mContext;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            LinearLayout mLinearLayout;
            public ViewHolder(View rootView) {
                super(rootView);
                mLinearLayout = (LinearLayout) rootView.findViewById(R.id.item_content_container);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public ContentsAdapter(List<Displayable> myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ContentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_content, parent, false);
            mContext = parent.getContext();
            // set the view's size, margins, paddings and layout parameters
            ViewHolder viewHolder = new ViewHolder(rootView);
            return viewHolder;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            //holder.mTextView.setText(mDataset[position]);
            mDataset.get(position).displayContent(mContext, holder.mLinearLayout);
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void addDisplayable(Displayable displayable) {
            mDataset.add(displayable);
            notifyDataSetChanged();
        }
}
