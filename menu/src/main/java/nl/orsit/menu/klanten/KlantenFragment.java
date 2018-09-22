package nl.orsit.menu.klanten;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

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
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;
import nl.orsit.menu.data.MenuDataFragment;

public class KlantenFragment extends SpinnerFragment implements ServiceCallback {

    protected RecyclerView mRecyclerView;
    protected TextInputEditText mSearchView;
    protected KlantAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<KlantItem> mDataset;
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
        rootView = inflater.inflate(R.layout.klanten, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.klantenRecycleView);
        mRecyclerView.addOnItemTouchListener(
                new ListTouchListener(getActivity(), mRecyclerView ,new ListTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String kid = mDataset.get(position).getKey();
                        System.out.println("Klant gekozen: " + kid);
                        savePreference(kid);
                        System.out.println("short:" + mDataset.get(position).getKey());
                        MenuDataInterface activity = (MenuDataInterface) getActivity();
                        activity.userDataChanged(MenuDataInterface.CHANGED.KID);
                        activity.tabObjecten();
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        String kid = mDataset.get(position).getKey();
                        savePreference(kid);
                        System.out.println("long:" + mDataset.get(position).getKey());
                        // do whatever
                    }

                    private void savePreference(String kid) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE).edit();
                        editor.putString("kid", kid);
                        editor.apply();
                    }
                })
        );
        mSearchView = (TextInputEditText) rootView.findViewById(R.id.searchKlant);
        mSearchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    loadDataset();
                    return true;
                }
                return false;
            }
        });


        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new KlantAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    public void loadDataset() {
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
            PhpParams params = new PhpParams();
            params.add("bid", prefs.getString("bid", ""));
            if (mSearchView != null) {
                params.add("searchKlant", mSearchView.getText().toString());
            }
            this.mTask = new BackendServiceCall(this, "javaSearchKlanten", "default", params);
            this.mTask.execute();
            mDataset = new ArrayList<>();
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
                mDataset.add(new KlantItem(obj.getString("kid"), obj));
            }
            mAdapter.setDataSet(mDataset);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getProgressView() {
        return rootView.findViewById(R.id.klant_progress);
    }

    @Override
    public View getParentView() {
        return rootView;
    }
}
