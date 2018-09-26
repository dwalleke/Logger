package nl.orsit.menu.objecten;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.List;

import nl.orsit.menu.R;
import nl.orsit.menu.klanten.KlantItem;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ViewHolder> {
    private List<ObjectItem> mDataSet;

    public void setDataSet(List<ObjectItem> dataSet) {
        this.mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewObjectOmschrijving;
        private final TextView textViewObjectMerkType;

        public ViewHolder(View v) {
            super(v);
            textViewObjectOmschrijving = (TextView) v.findViewById(R.id.objectOmschrijving);
            textViewObjectMerkType = (TextView) v.findViewById(R.id.objectMerkType);

        }

        public TextView getOmschrijving() { return this.textViewObjectOmschrijving; }
        public TextView getMerkType() { return this.textViewObjectMerkType; }

    }

    public ObjectAdapter(List<ObjectItem> dataSet) {
        mDataSet = dataSet;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_object, viewGroup, false);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        View links = v.findViewById(R.id.objectOmschrijving);
        View rechts = v.findViewById(R.id.objectMerkType);
        ViewGroup.LayoutParams lpLinks = links.getLayoutParams();
        lpLinks.width = lp.width / 2;
        ViewGroup.LayoutParams lpRechts = rechts.getLayoutParams();
        lpRechts.width = lp.width / 2;
        links.setLayoutParams(lpLinks);
        rechts.setLayoutParams(lpRechts);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        ObjectItem item = mDataSet.get(position);
        viewHolder.getOmschrijving().setText(item.getSoort());
        viewHolder.getMerkType().setText(item.getMerk() + " / " + item.getType());
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