package nl.orsit.menu.klanten;

import android.support.annotation.ColorInt;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

import nl.orsit.menu.ListItem;
import nl.orsit.menu.R;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class KlantAdapter extends RecyclerView.Adapter<KlantAdapter.ViewHolder> {
    private List<KlantItem> mDataSet;

    public void setDataSet(List<KlantItem> dataSet) {
        this.mDataSet = dataSet;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewKlantNaam;
        private final TextView textViewKlantPlaats;

        public ViewHolder(View v) {
            super(v);
            textViewKlantNaam = (TextView) v.findViewById(R.id.klantNaam);
            textViewKlantPlaats = (TextView) v.findViewById(R.id.klantPlaats);

        }

        public TextView getNaam() { return this.textViewKlantNaam; }
        public TextView getPlaats() { return this.textViewKlantPlaats; }

    }

    public KlantAdapter(List<KlantItem> dataSet) {
        mDataSet = dataSet;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_klant, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        KlantItem klantItem = mDataSet.get(position);
        viewHolder.getNaam().setText(klantItem.getNaam());
        viewHolder.getPlaats().setText(klantItem.getPlaats());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}