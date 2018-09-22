package nl.orsit.menu.objecten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerFragment;
import nl.orsit.menu.ListTouchListener;
import nl.orsit.menu.ListAdapter;
import nl.orsit.menu.ListItem;
import nl.orsit.menu.MenuActivity;
import nl.orsit.menu.R;

public class ObjectenFragment extends SpinnerFragment implements ServiceCallback {

    protected RecyclerView mRecyclerView;
    protected TextInputEditText mSearchView;
    protected ListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ListItem> mDataset;
    private BackendServiceCall mTask;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.objecten, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.objectenRecycleView);
        mRecyclerView.addOnItemTouchListener(
                new ListTouchListener(getActivity(), mRecyclerView ,new ListTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String kid = mDataset.get(position).getKey();
                        savePreference(kid);
                        System.out.println("short:" + mDataset.get(position).getKey());
                        Intent intent = new Intent(getActivity(), MenuActivity.class);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        String kid = mDataset.get(position).getKey();
                        savePreference(kid);
                        System.out.println("long:" + mDataset.get(position).getKey());
                        // do whatever
                    }

                    private void savePreference(String obj) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE).edit();
                        editor.putString("obj", obj);
                        editor.apply();

                    }
                })
        );
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ListAdapter(mDataset, R.layout.list_item_object, R.id.listItemObjectTextView);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    private void loadDataset() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
        PhpParams params = new PhpParams();
        params.add("bid", prefs.getString("bid", ""));
        params.add("kid", prefs.getString("kid", ""));
        this.mTask = new BackendServiceCall(this, "javaGetObjecten", "default", params);
        this.mTask.execute();
        mDataset = new ArrayList<>();
    }

    @Override
    public void cancel(PhpResult phpResult) {
        this.mTask = null;
        showProgress(false);
    }

    @Override
    public void finish(PhpResult phpResult) {
        this.mTask = null;
        showProgress(false);
        String rows = phpResult.getResults().get("rows");
        try {
            JSONArray arr = new JSONArray(rows);
            mDataset = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                boolean hasSerienr = obj.getString("serienr").length() != 0;
                String object = obj.getString("omschr") + "\n" +
                        obj.getString("merk") + " / " + obj.getString("type") + "\n";
                if (hasSerienr) {
                    object += obj.getString("serienr") + " ";
                }
                mDataset.add(new ListItem(obj.getString("qr"), object));
            }
            System.out.println("Data found: " + mDataset.size());
            mAdapter.setDataSet(mDataset);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getProgressView() {
        return rootView.findViewById(R.id.object_progress);
    }

    @Override
    public View getParentView() {
        return rootView;
    }
}
