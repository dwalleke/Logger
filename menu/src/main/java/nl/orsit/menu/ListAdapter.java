package nl.orsit.menu;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListItem> mDataSet;
    private int listItemView;
    private int listItemTextView;

    public void setDataSet(List<ListItem> dataSet) {
        this.mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v, int listItemView) {
            super(v);
            textView = (TextView) v.findViewById(listItemView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    public ListAdapter(List<ListItem> dataSet, int listItemView, int listItemTextView) {
        mDataSet = dataSet;
        this.listItemView = listItemView;
        this.listItemTextView = listItemTextView;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(this.listItemView, viewGroup, false);

        return new ViewHolder(v, this.listItemTextView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(mDataSet.get(position).getValue());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}