package nl.orsit.menu.logs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ScrollingTableItem;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerFragment;
import nl.orsit.menu.ListTouchListener;
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;
import nl.orsit.menu.data.LogTypes;
import nl.orsit.menu.data.MasterInvoer;
import nl.orsit.menu.data.MasterStatus;
import nl.orsit.menu.util.CameraUtil;
import nl.orsit.menu.util.DialogUtil;
import nl.orsit.menu.util.zoom.ImageViewTouch;
import nl.orsit.menu.util.zoom.ImageViewTouchBase;
import nl.orsit.menu.util.zoom.utils.BitmapUtils;

public class LogsFragment extends SpinnerFragment implements ServiceCallback {

    protected RecyclerView mRecyclerView;
    protected LogsAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected List<LogItem> mDataset;
    private BackendServiceCall mTask;
    private View rootView;
    private LogTypes masterLogData;
    private Context logDialogContext;
    private TableLayout dialogTableLayout;
    private LogItem currentData;
    private AlertDialog aDialog;
    private DialogUtil dialogUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDataset();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.logs, container, false);
        masterLogData = ((MenuDataInterface)getActivity()).getLogTypes();
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.logsRecycleView);
        mRecyclerView.addOnItemTouchListener(
                new ListTouchListener(getActivity(), mRecyclerView ,new ListTouchListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String log = mDataset.get(position).getKey();
                        savePreference(log);
                        currentData = mDataset.get(position);
                        createDialogTable(true);
                   }

                    @Override public void onLongItemClick(View view, int position) {
                        String log = mDataset.get(position).getKey();
                        savePreference(log);
                        currentData = mDataset.get(position);
                        createDialogTable(false);
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
                mDataset.add(new LogItem(obj, parent.getLogTypes()));
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


    private TableRow prepareFixedHeader(TableLayout header) {
        if (currentData.isChecked()) {
            header.setBackgroundResource(R.color.ok);
        } else {
            header.setBackgroundResource(R.color.nok);
        }
        TextView headerText = new TextView(getContext());
        MasterStatus ms = masterLogData.getUserStatus(currentData.getType(), currentData.getStatus());
        headerText.setText(ms.getOmschrijving());

        TableRow row = new TableRow(getContext());
        row.addView(headerText);
        return row;
    }

    private TableRow prepareChoiceHeader(TableLayout header) {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
        final String type = prefs.getString("obj_soort", "1");

        Spinner spinner = new Spinner(getContext());
        Collection<MasterStatus> statusList = masterLogData.getUserStatusList(type);
        List<MasterStatus> list = new ArrayList<MasterStatus>();
        MasterStatus dummyStatus = new MasterStatus("Selecteer een activiteit");
        list.add(dummyStatus);
        list.addAll(statusList);
        ArrayAdapter<MasterStatus> dataAdapter = new ArrayAdapter<MasterStatus>(getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            int check = 0;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (++check > 1) {
                    MasterStatus ms = (MasterStatus) parent.getSelectedItem();
                    if (ms.getId() != null) {
                        currentData.setStatus(ms.getId());
                        currentData.setType(type);
                        aDialog.dismiss();
                        createDialogTable(false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // no action
            }
        });

        TableRow row = new TableRow(getContext());
        row.addView(spinner);
        return row;
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

    private void addImage(DialogUtil dialogUtil, String value, boolean viewOnly, MasterInvoer mi, LogItem data) {
        View label = dialogUtil.createLabel(mi.getOmschrijving());
        if (value != null) {
            View view = createBekijkFotoButton(dialogUtil, mi, data.getLogs(), value);
            dialogUtil.addRows(dialogTableLayout, label, view);
        } else {
            if (!viewOnly) {
                View view = createMaakFotoButton(dialogUtil, mi, data.getLogs());
                dialogUtil.addRows(dialogTableLayout, label, view);
            }
        }
    }

    private void addLogItemList(DialogUtil dialogUtil, boolean viewOnly) {
        List<MasterInvoer> list = masterLogData.getUserInvoerList(currentData.getStatus());
        for (MasterInvoer mi : list) {
            String value = getValue(mi, currentData);
            ScrollingTableItem sti = new ScrollingTableItem(mi.getOmschrijving(), mi.getId(), value);
            switch (masterLogData.getLogTypeByCode(mi.getInvoerType())) {
                case Choice:
                    break;
                case Image:
                    addImage(dialogUtil, value, viewOnly, mi, currentData);
                    break;
                case MultiText:
                    dialogUtil.addMultiText(sti, viewOnly, dialogTableLayout);
                    break;
                case Number:
                    dialogUtil.addNumber(sti, viewOnly, dialogTableLayout);
                    break;
                case Text:
                    dialogUtil.addText(sti, viewOnly, dialogTableLayout);
                    break;
            }
        }
    }

    public void createInvoerChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Context myContext = builder.getContext();
        final View alertView = LayoutInflater.from(myContext).inflate(R.layout.invoer_dialog, null);
        builder.setView(alertView);
        DialogUtil dialogUtil = new DialogUtil() {
            @Override
            public Resources getResources() {
                return LogsFragment.this.getResources();
            }

            @Override
            public Context getContext() {
                return myContext;
            }

            @Override
            public void action() {
                // what to do when we hit the 'save' button?
            }
        };

        TableLayout invoerHeader = (TableLayout)alertView.findViewById(R.id.invoerHeader);
        dialogUtil.prepareHeader(invoerHeader);
        invoerHeader.addView(prepareChoiceHeader(invoerHeader));
        builder.setCancelable(true);
        aDialog = builder.create();
        aDialog.show();
        dialogUtil.resizeHeaderDialog(aDialog, invoerHeader);

    }

    public void createDialogTable(boolean viewOnly) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        final Context myContext = builder.getContext();
        final View alertView = LayoutInflater.from(myContext).inflate(R.layout.log_dialog, null);
        builder.setView(alertView);
        DialogUtil dialogUtil = new DialogUtil() {
            @Override
            public Resources getResources() {
                return LogsFragment.this.getResources();
            }

            @Override
            public Context getContext() {
                return myContext;
            }

            @Override
            public void action() {
                // what do do when we click 'ok'?
            }
        };

        this.dialogTableLayout = (TableLayout)alertView.findViewById(R.id.logTableLayout);
        ScrollView scrollView = (ScrollView)alertView.findViewById(R.id.scrollTable);
        TableLayout logHeader = (TableLayout)alertView.findViewById(R.id.logHeader);
        TableLayout logFooter = (TableLayout)alertView.findViewById(R.id.logFooter);

        dialogUtil.prepareHeader(logHeader);
        logHeader.addView(prepareFixedHeader(logHeader));
        addLogItemList(dialogUtil, viewOnly);


        builder.setCancelable(true);
        aDialog = builder.create();
        if (currentData.getKey() == null) {
            dialogUtil.prepareFooter("Toevoegen", logFooter, aDialog, viewOnly);
        } else {
            dialogUtil.prepareFooter("Wijzigen", logFooter, aDialog, viewOnly);
        }

        aDialog.show();
        dialogUtil.resizeDialog(aDialog, scrollView, logHeader, logFooter);
    }

    private Button createMaakFotoButton(final DialogUtil dialogUtil, MasterInvoer mi, Map<String, LogItemType> logs) {
        Button button = dialogUtil.createCleanButton("maak foto");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                Context dialogContext = builder.getContext();
                final View cameraView = LayoutInflater.from(dialogContext).inflate(R.layout.camera_dialog, null);
                Button takePictureButton = cameraView.findViewById(R.id.cameraButton);
                TextureView textureView = cameraView.findViewById(R.id.cameraTexture);
                builder.setView(cameraView);
                builder.setCancelable(true);
                final AlertDialog cameraDialog = builder.create();
                cameraDialog.show();
                dialogUtil.resizeFooterDialog(cameraDialog, textureView, takePictureButton);
                final CameraUtil cameraUtil = new CameraUtil(getActivity(), cameraDialog, textureView);
                takePictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cameraUtil.takePicture();
                    }
                });
                cameraUtil.setImageAvailableLister(new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Image image = null;
                        try {
                            image = reader.acquireNextImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            buffer.get(bytes);
                            System.out.println("Image captured: " + bytes.length);

                            String foto = Base64.encodeToString(bytes, bytes.length);
                            System.out.println("-------------------------------- FOTO TAKEN -----------------------");
                            System.out.println(foto);
                            //TODO: do something with these bytes. This is the picture!
                        } finally {
                            if (image != null) {
                                image.close();
                            }
                            cameraUtil.closeCamera();
                            cameraDialog.dismiss();
                        }
                    }
                });
                cameraUtil.startCamera();
            }
        });
        return button;
    }

    private Button createBekijkFotoButton(final DialogUtil dialogUtil, MasterInvoer mi, Map<String, LogItemType> logs, final String foto) {
        Button button = dialogUtil.createCleanButton("bekijk foto");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                Context dialogContext = builder.getContext();
                final View pictureView = LayoutInflater.from(dialogContext).inflate(R.layout.picture_dialog, null);

                Button pictureButton = pictureView.findViewById(R.id.pictureButton);
                ImageViewTouch pictureImageView = pictureView.findViewById(R.id.pictureView);
                final String pureBase64Encoded = foto.substring(foto.indexOf(",")  + 1);

                byte[] decodedString = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                builder.setView(pictureView);
                builder.setCancelable(true);
                final AlertDialog pictureDialog = builder.create();
                pictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pictureDialog.dismiss();
                    }
                });
                pictureDialog.show();
                pictureImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_IF_BIGGER);
                dialogUtil.resizeFooterDialog(pictureDialog, pictureImageView, pictureButton);
                ViewGroup.LayoutParams lp = pictureImageView.getLayoutParams();
                int width = lp.width - 30;
                int height = lp.height - 30;
                System.out.println("ImageView [w/h]: " + width + "/" + height);
                System.out.println("BM before resize [w/h]: " + bm.getWidth() + " / " + bm.getHeight());
                BitmapUtils.resizeBitmap(bm, width, height);
                System.out.println("BM after resize [w/h]: " + bm.getWidth() + " / " + bm.getHeight());
                pictureImageView.setImageBitmap(bm, null, -1, -1);
            }
        });

        return button;
    }


    public void addLog() {
        currentData = new LogItem(masterLogData);
        createInvoerChoiceDialog();
    }


}
