package nl.orsit.base;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class CameraPermission {

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static CameraPermission instance;
    private CameraPermission() {

    }

    public static CameraPermission getInstance() {
        if (instance == null) {
            instance = new CameraPermission();
        }
        return instance;
    }

    public void checkCameraPermission(Activity mActivity) {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{ Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CAMERA_PERMISSION);
            return;
        }
    }
}
