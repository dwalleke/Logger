package nl.orsit.menu.logs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerFragment;
import nl.orsit.menu.ListTouchListener;
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;
import nl.orsit.menu.data.LogTypes;
import nl.orsit.menu.data.MasterInvoer;
import nl.orsit.menu.data.MasterStatus;

public class LogsFragment extends SpinnerFragment implements ServiceCallback {

    protected RecyclerView mRecyclerView;
    protected LogsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<LogItem> mDataset;
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
        rootView = inflater.inflate(R.layout.logs, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.logsRecycleView);
        mRecyclerView.addOnItemTouchListener(
                new ListTouchListener(getActivity(), mRecyclerView ,new ListTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String log = mDataset.get(position).getKey();
                        savePreference(log);
                        dialogTable(((MenuDataInterface)getActivity()).getLogTypes(), mDataset.get(position), true);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        String log = mDataset.get(position).getKey();
                        savePreference(log);
                        dialogTable(((MenuDataInterface)getActivity()).getLogTypes(), mDataset.get(position), false);
                    }

                    private void savePreference(String kid) {
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE).edit();
                        editor.putString("log", kid);
                        editor.apply();

                    }
                })
        );
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LogsAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }


    /**
     * Generates Strings for RecyclerView's adapter. This data would usually come
     * from a local content provider or remote server.
     */
    public void loadDataset() {
        if (getActivity() != null) {
            if (mAdapter != null) {
                mAdapter.setDataSet(new ArrayList<LogItem>());
                mAdapter.notifyDataSetChanged();
            }
            SharedPreferences prefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
            PhpParams params = new PhpParams();
            params.add("bid", prefs.getString("bid", ""));
            params.add("kid", prefs.getString("kid", ""));
            params.add("obj", prefs.getString("obj", ""));
            this.mTask = new BackendServiceCall(this, "javaGetLogs", "default", params);
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
        MenuDataInterface parent = (MenuDataInterface) getActivity();
        try {
            JSONArray arr = new JSONArray(rows);
            mDataset = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                mDataset.add(new LogItem(obj, parent));
            }
            mAdapter.setDataSet(mDataset);
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View getProgressView() {
        return rootView.findViewById(R.id.logs_progress);
    }

    @Override
    public View getParentView() {
        return rootView;
    }

    private void prepareHeader(View alertView, LogItem data) {
        LinearLayout header = alertView.findViewById(R.id.logHeader);
        if (data.isChecked()) {
            header.setBackgroundResource(R.color.ok);
        } else {
            header.setBackgroundResource(R.color.nok);
        }
        TextView headerStatus = (TextView)alertView.findViewById(R.id.headerStatus);
        MasterStatus ms = data.getLogTypes().getUserStatus(data.getType(), data.getStatus());
        headerStatus.setText(ms.getOmschrijving());
    }


    private void addRows(TableLayout ll, Context dialogContext, View label, View view) {
        addRows(ll, dialogContext, label, view, true);
    }

    private void addRows(TableLayout ll, Context dialogContext, View label, View view, boolean singleRow) {
        TableRow row = new TableRow(dialogContext);
        ll.addView(prepareRow(row));
        if (singleRow) {
            row.addView(prepareCell(label, 1));
            row.addView(prepareCell(view, 1));
        } else {
            row.addView(prepareCell(label, 2));
            TableRow secondRow = new TableRow(dialogContext);
            prepareRow(secondRow);
            secondRow.addView(prepareCell(view, 2));
            ll.addView(secondRow);
        }

    }

    private String getValue(MasterInvoer mi, LogItem data) {
        if (data.getLogs().containsKey(mi.getId())) {
            String value = data.getLogs().get(mi.getId()).getValue();
            if ("".equals(value)) {
                return null;
            }
            return value;
        }
        return null;
    }

    private void addImage(String value, boolean viewOnly, Context dialogContext, TableLayout tableLayout, MasterInvoer mi, LogItem data) {
        View label = createLabel(mi.getOmschrijving());
        if (value != null) {
            View view = createBekijkFotoButton(mi, data.getLogs(), value);
            addRows(tableLayout, dialogContext, label, view);
        } else {
            if (!viewOnly) {
                View view = createMaakFotoButton(mi, data.getLogs());
                addRows(tableLayout, dialogContext, label, view);
            }
        }
    }

    private void addMultiText(String value, boolean viewOnly, Context dialogContext, TableLayout tableLayout, MasterInvoer mi, LogItem data) {
        View label = createLabel(mi.getOmschrijving());
        if (value != null) {
            View view = createTextView(value);
            view.setMinimumHeight(200);
            addRows(tableLayout, dialogContext, label, view, false);
        } else {
            if (!viewOnly) {
                View view = createInputView(mi.getId(), InputType.TYPE_CLASS_TEXT);
                view.setMinimumHeight(200);
                addRows(tableLayout, dialogContext, label, view, false);
            }
        }

    }

    public void dialogTable(LogTypes logTypes, LogItem data, boolean viewOnly) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Context dialogContext = builder.getContext();
        final View alertView = LayoutInflater.from(dialogContext).inflate(R.layout.show_log_dialog, null);
        builder.setView(alertView);

        final TableLayout tableLayout = (TableLayout)alertView.findViewById(R.id.logTableLayout);
        prepareHeader(alertView, data);

        for (MasterInvoer mi : logTypes.getUserInvoerList(data.getStatus())) {
            String value = getValue(mi, data);
            switch (logTypes.getLogTypeByCode(mi.getInvoerType())) {
                case Choice:
                    break;
                case Image:
                    addImage(value, viewOnly, dialogContext, tableLayout, mi, data);
                    break;
                case MultiText:
                    addMultiText(value, viewOnly, dialogContext, tableLayout, mi, data);
                    break;
                case Number:
                    addNumber(value, viewOnly, dialogContext, tableLayout, mi, data);
                    break;
                case Text:
                    addText(value, viewOnly, dialogContext, tableLayout, mi, data);
                    break;
            }
        }

        builder.setCancelable(true);
        final AlertDialog alertDialog = builder.create();
        prepareFooter(alertView, alertDialog, viewOnly);

        alertDialog.show();

        resizeDialog(alertDialog, alertView);

        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_edit_shape);
    }

    private void addText(String value, boolean viewOnly, Context dialogContext, TableLayout tableLayout, MasterInvoer mi, LogItem data) {
        View label = createLabel(mi.getOmschrijving());
        if (value != null) {
            View view = createTextView(value);
            addRows(tableLayout, dialogContext, label, view);
        } else {
            if (!viewOnly) {
                View view = createInputView(mi.getId(), InputType.TYPE_CLASS_TEXT);
                addRows(tableLayout, dialogContext, label, view);
            }
        }

    }

    private void addNumber(String value, boolean viewOnly, Context dialogContext, TableLayout tableLayout, MasterInvoer mi, LogItem data) {

        View label = createLabel(mi.getOmschrijving());
        if (value != null) {
            View view = createTextView(value);
            addRows(tableLayout, dialogContext, label, view);
        } else {
            if (!viewOnly) {
                View view = createInputView(mi.getId(), InputType.TYPE_NUMBER_FLAG_DECIMAL);
                addRows(tableLayout, dialogContext, label, view);
            }
        }

    }

    private void resizeDialog(AlertDialog alertDialog, View alertView) {
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);

        // dialog
        alertDialog.getWindow().setLayout(width, height);
        width -= 30;

        // scrollview
        ScrollView scrollView = (ScrollView)alertView.findViewById(R.id.scrollTable);
        ViewGroup.LayoutParams lpScrollView = scrollView.getLayoutParams();

        // logheader
        TableLayout logHeader = (TableLayout)alertView.findViewById(R.id.logHeader);
        ViewGroup.LayoutParams lpHeader = logHeader.getLayoutParams();
        lpHeader.width = width;
        logHeader.setLayoutParams(lpHeader);

        // logfooter
        TableLayout logFooter = (TableLayout)alertView.findViewById(R.id.logFooter);
        ViewGroup.LayoutParams lpFooter = logFooter.getLayoutParams();
        lpFooter.width = width;
        logFooter.setLayoutParams(lpFooter);

        lpScrollView.width = width;
        lpScrollView.height = height - (lpHeader.height + lpFooter.height);
        scrollView.setLayoutParams(lpScrollView);


    }

    private Button createCleanButton(String text) {
        Button button = new Button(getContext());
        button.setBackgroundResource(R.drawable.rounded_button_shape);
        button.setTextColor(Color.parseColor("#ffffff"));
        button.setText(text);
        return button;
    }

    private void prepareFooter(View alertView, final AlertDialog alertDialog, boolean viewOnly) {
        TableLayout footer = (TableLayout) alertView.findViewById(R.id.logFooter);
        TableRow row = new TableRow(getContext());
        if (!viewOnly) {
            Button toevoegen = createCleanButton("toevoegen");
            Button annuleren = createCleanButton("annuleren");
            annuleren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            row.addView(toevoegen);
            row.addView(annuleren);
        } else {
            Button annuleren = createCleanButton("sluiten");
            annuleren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            row.addView(annuleren);
        }
        footer.addView(row);

    }

    private TableRow prepareRow(TableRow row) {
        row.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        row.setPadding(0, 25, 0, 0);
        return row;
    }

    private View prepareCell(View view, int span) {
        view.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT));
        view.setPadding(25, 5, 25, 5);
        TableRow.LayoutParams lp = (TableRow.LayoutParams) view.getLayoutParams();
        lp.span = span;
        view.setLayoutParams(lp);
        return view;
    }

    private TextView createLabel(String text) {
        TextView label = new TextView(getContext());
        label.setText(text);
        return label;
    }

    private View createTextView(String value) {
        TextView text = new TextView(getContext());
        text.setBackgroundResource(R.drawable.rounded_view_shape);
        text.setText(value);
        return text;
    }

    private View createInputView(String id, int inputType) {
        TextInputEditText input = new TextInputEditText(getContext());
        input.setId(Integer.parseInt(id));
        input.setInputType(inputType);
        input.setBackgroundResource(R.drawable.rounded_edit_shape);
        return input;


    }

    private Button createMaakFotoButton(MasterInvoer mi, Map<String, LogItemType> logs) {
        Button button = createCleanButton("maak foto");

        return button;
    }

    private Button createBekijkFotoButton(MasterInvoer mi, Map<String, LogItemType> logs, String foto) {
        Button button = createCleanButton("bekijk foto");

        return button;
    }


}
