package nl.orsit.menu.klanten;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;
import nl.orsit.menu.util.HuisnummerValidator;
import nl.orsit.menu.util.MenuInfoReloader;
import nl.orsit.menu.util.PostcodeValidator;

public class KlantenFragment extends SpinnerFragment implements ServiceCallback {

    protected RecyclerView mRecyclerView;
    protected TextInputEditText mSearchKlant;
    protected TextInputEditText mSearchPostcode;
    protected TextInputLayout mSearchPostcodeWrapper;
    protected PostcodeValidator mPostcodeValidator;
    protected TextInputEditText mSearchHuisnummer;
    protected TextInputLayout mSearchHuisnummerWrapper;
    protected HuisnummerValidator mHuisnummerValidator;
    protected KlantAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<KlantItem> mDataset;
    private BackendServiceCall mTask;
    private View rootView;

    public KlantenFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (MenuInfoReloader.getLevel() == MenuDataInterface.LEVEL.NORMAAL) {
            rootView = inflater.inflate(R.layout.klanten_postcode_huisnummer, container, false);
        } else {
            rootView = inflater.inflate(R.layout.klanten_naam_postcode_plaats, container, false);
            createAddButton();
        }
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.klantenRecycleView);
        mRecyclerView.addOnItemTouchListener(
                new ListTouchListener(getActivity(), mRecyclerView ,new ListTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        if (mDataset.size() > position) {
                            String kid = mDataset.get(position).getKey();
                            if (kid != null) {
                                MenuInfoReloader.setUserData(null, null, kid, "");
                                MenuDataInterface activity = (MenuDataInterface) getActivity();
                                activity.getTabAdapter().setObjectenFragment();
                            }
                        }
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        if (MenuInfoReloader.getLevel() != MenuDataInterface.LEVEL.NORMAAL) {
                            if (mDataset.size() > position) {
                                String kid = mDataset.get(position).getKey();
                                if (kid != null) {
                                    MenuInfoReloader.setUserData(null, null, kid, "");
                                    editKlant();
                                }
                            }
                        }
                    }

                })
        );
        if (MenuInfoReloader.getLevel() != MenuDataInterface.LEVEL.NORMAAL) {
            mSearchKlant = (TextInputEditText) rootView.findViewById(R.id.searchKlant);
            mSearchKlant.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                    if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                        loadDataset();
                        return true;
                    }
                    return false;
                }
            });
        } else {
            mSearchPostcodeWrapper = (TextInputLayout) rootView.findViewById(R.id.searchPostcodeWrapper);
            mPostcodeValidator = new PostcodeValidator(mSearchPostcodeWrapper);
            mSearchPostcode = (TextInputEditText) rootView.findViewById(R.id.searchPostcode);
            mSearchHuisnummerWrapper = (TextInputLayout) rootView.findViewById(R.id.searchHuisnummerWrapper);
            mHuisnummerValidator = new HuisnummerValidator(mSearchHuisnummerWrapper);
            mSearchHuisnummer = (TextInputEditText) rootView.findViewById(R.id.searchHuisnummer);
        }
        Button button = (Button) rootView.findViewById(R.id.zoekKlantButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataset();
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
            if (MenuInfoReloader.getLevel() == MenuDataInterface.LEVEL.NORMAAL) {
                if (mSearchPostcode != null && mSearchHuisnummer != null) {
                    if (mPostcodeValidator.validate(mSearchPostcode.getText().toString()) && mHuisnummerValidator.validate(mSearchHuisnummer.getText().toString())) {
                        params.add("searchPostcode", mSearchPostcode.getText().toString().toUpperCase());
                        params.add("searchHuisnummer", mSearchHuisnummer.getText().toString());
                        this.mTask = new BackendServiceCall(this, "javaSearchKlant", "default", params);
                        this.mTask.execute();
                    }
                }
            } else {
                if (mSearchKlant != null) {
                    params.add("searchKlant", mSearchKlant.getText().toString());
                }
                this.mTask = new BackendServiceCall(this, "javaSearchKlanten", "default", params);
                this.mTask.execute();
            }
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
            if (mDataset.size() == 0) {
                mDataset.add(new KlantItem());
            }
            mAdapter.setDataSet(mDataset);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getProgressView() {
        return getActivity().findViewById(R.id.progress);
    }

    @Override
    public View getParentView() {
        return rootView;
    }

    public void editKlant() {

    }

    private void createAddButton() {
        FloatingActionButton add = (FloatingActionButton) getActivity().findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

    }


}
