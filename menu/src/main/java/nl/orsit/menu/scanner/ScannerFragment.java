package nl.orsit.menu.scanner;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import nl.orsit.base.BackendServiceCall;
import nl.orsit.base.PhpParams;
import nl.orsit.base.PhpResult;
import nl.orsit.base.ServiceCallback;
import nl.orsit.base.SpinnerFragment;
import nl.orsit.menu.MenuDataInterface;
import nl.orsit.menu.R;
import nl.orsit.menu.util.MenuInfoReloader;

public class ScannerFragment extends SpinnerFragment implements ServiceCallback {
    private CodeScanner mCodeScanner;
    private BackendServiceCall mTask;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final Activity activity = getActivity();
        rootView = inflater.inflate(R.layout.scanner, container, false);
        System.out.println("Creating scanner");
        CodeScannerView scannerView = rootView.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(activity, activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences prefs = getActivity().getSharedPreferences("UserData", getActivity().MODE_PRIVATE);
                        PhpParams params = new PhpParams();
                        params.add("qrc", result.getText());
                        mTask = new BackendServiceCall(ScannerFragment.this, "javaScanObject", "default", params);
                        mTask.execute();
                        mCodeScanner.startPreview();
                    }
                });
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
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
        if (!phpResult.getErrors().containsKey("error")) {
            String obj = phpResult.getResults().get("obj");
            String kid = phpResult.getResults().get("kid");
            MenuInfoReloader.setUserData(null, null, kid, obj);
            MenuInfoReloader.getActivity().getTabAdapter().setLogsFragment(true, true);
        } else {
            MenuInfoReloader.getActivity().getTabAdapter().setKlantenFragment(true, false);
            Toast.makeText(MenuInfoReloader.getActivity(), "Deze QR-code is onbekend.\nZoek op postcode en adres naar uw klant" , Toast.LENGTH_LONG ).show();
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

    @Override
    public void loadDataset(Activity activity) {

    }

    @Override
    public void resetData() {

    }

}