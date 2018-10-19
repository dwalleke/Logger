package nl.orsit.menu.logs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

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
import nl.orsit.menu.util.MenuInfoReloader;
import nl.orsit.menu.util.zoom.ImageViewTouch;
import nl.orsit.menu.util.zoom.ImageViewTouchBase;
import nl.orsit.menu.util.zoom.utils.BitmapUtils;

public class LogFragment extends SpinnerFragment implements ServiceCallback {

    protected LogItem mDataset;
    private BackendServiceCall mTask;
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.log_dialog, container, false);

        return rootView;
    }

    @Override
    public void cancel(PhpResult phpResult) {

    }

    @Override
    public void finish(PhpResult phpResult) {

    }

    @Override
    public View getProgressView() {
        return MenuInfoReloader.getActivity().findViewById(R.id.progress);
    }

    @Override
    public View getParentView() {
        return rootView;
    }

    @Override
    public void loadDataset(Activity activity) {

    }

    @Override
    public void resetData() {
        mDataset = null;
    }
}
