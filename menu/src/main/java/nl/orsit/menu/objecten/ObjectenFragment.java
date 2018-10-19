package nl.orsit.menu.objecten;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;
import nl.orsit.menu.klanten.KlantItem;
import nl.orsit.menu.util.MenuInfoReloader;

public class ObjectenFragment extends SpinnerFragment implements ServiceCallback {

    protected RecyclerView mRecyclerView;
    protected ObjectenAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<ObjectItem> mDataset;
    private BackendServiceCall mTask;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.objecten, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.objectenRecycleView);
        mRecyclerView.addOnItemTouchListener(
                new ListTouchListener(getActivity(), mRecyclerView ,new ListTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (mDataset.size() > position) {
                            ObjectItem chosen = mDataset.get(position);
                            pickObject(chosen);
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        if (mDataset.size() > position) {
                            String obj = mDataset.get(position).getKey();
                            if (obj != null) {
                                String type = mDataset.get(position).getType();
                                MenuInfoReloader.setUserData(null, null, null, obj);
                                MenuInfoReloader.savePref("obj_soort", type);
                                // TODO long click
                            }
                        }
                    }

                })
        );
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mDataset = new ArrayList<>();
        mAdapter = new ObjectenAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        return rootView;
    }

    private void pickObject(ObjectItem chosen) {
        String obj = chosen.getKey();
        if (obj != null) {
            String type = chosen.getSoort();
            MenuInfoReloader.setUserData(null, null, null, obj);
            MenuInfoReloader.savePref("obj_soort", type);
            MenuDataInterface activity = (MenuDataInterface) getActivity();
            activity.getTabAdapter().setLogsFragment(true, true);
        }
    }

    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    public void loadDataset(Activity activity) {
        System.out.println("Loading dataset objecten");
        SharedPreferences prefs = activity.getSharedPreferences("UserData", activity.MODE_PRIVATE);
        PhpParams params = new PhpParams();
        params.add("bid", prefs.getString("bid", ""));
        params.add("kid", prefs.getString("kid", ""));
        this.mTask = new BackendServiceCall(this, "javaGetObjecten", "default", params);
        this.mTask.execute();
    }

    @Override
    public void resetData() {
        mDataset = new ArrayList<>();
        if (mAdapter != null) {
            mAdapter.setDataSet(mDataset);
            mAdapter.notifyDataSetChanged();
        }
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
                mDataset.add(new ObjectItem(((MenuDataInterface)getActivity()).getLogTypes(), obj));
            }
            if (mDataset.size() == 0) {
                Toast.makeText(MenuInfoReloader.getActivity(), "Geen resultaten gevonden." , Toast.LENGTH_LONG ).show();
            } else {
                if(mDataset.size() == 1) {
                    if (MenuInfoReloader.getLevel() == MenuDataInterface.LEVEL.NORMAAL) {
                        pickObject(mDataset.get(0));
                    }
                }
            }


            mAdapter.setDataSet(mDataset);
            mAdapter.notifyDataSetChanged();
            System.out.println("Loaded dataset objecten");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getProgressView() {
        return MenuInfoReloader.getActivity().findViewById(R.id.progress);
    }

    @Override
    public View getParentView() {
        return rootView;
    }

    public void addObject() {
    }

}
