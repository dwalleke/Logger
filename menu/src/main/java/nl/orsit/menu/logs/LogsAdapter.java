package nl.orsit.menu.logs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import nl.orsit.menu.R;
import nl.orsit.menu.data.MasterStatus;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class LogsAdapter extends RecyclerView.Adapter<LogsAdapter.ViewHolder> {
    private List<LogItem> mDataSet; // One log for each item in the dataset.

    public void setDataSet(List<LogItem> arg) {
        this.mDataSet = arg;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView datum;
        private TextView status;
        private ImageView checked;

        public TextView getDatum() {
            return this.datum;
        }

        public TextView getStatus() {
            return this.status;
        }

        public ImageView getChecked() {
            return this.checked;
        }

        public ViewHolder(View v) {
            super(v);
            datum = (TextView) v.findViewById(R.id.logDatum);
            status = (TextView) v.findViewById(R.id.logStatus);
            checked = (ImageView) v.findViewById(R.id.logChecked);
        }
        
    }

    public LogsAdapter(List<LogItem> dataSet) {
        mDataSet = dataSet;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_log, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        LogItem item = mDataSet.get(position);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.getDatum().setText(format.format(item.getDatum()));
        MasterStatus status = item.getLogTypes().getUserStatus(item.getType(), item.getStatus());
        viewHolder.getStatus().setText(status.getOmschrijving());
        if (item.isChecked()) {
            viewHolder.getChecked().setImageResource(R.drawable.yes);
        } else {
            viewHolder.getChecked().setImageResource(R.drawable.no);
        }
        if (position % 2 == 1) {
            viewHolder.itemView.setBackgroundResource(R.color.rowEven);

        } else {
            viewHolder.itemView.setBackgroundResource(R.color.rowOdd);
        }

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}