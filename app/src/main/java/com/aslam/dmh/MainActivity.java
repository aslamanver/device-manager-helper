package com.aslam.dmh;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;

import com.aslam.dmh.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding(R.layout.activity_main);
        getBinding().txtVersion.setText("v" + BuildConfig.VERSION_NAME);
        getBinding().btnUpdate.setOnClickListener(v -> {
            if (isNetworkConnected()) {
                Intent broadcastIntent = new Intent("com.aslam.dmh.FROM_DM");
                broadcastIntent.setComponent(new ComponentName("com.aslam.dmh", "com.aslam.dmh.broadcasts.HelperBroadcastReceiver"));
                broadcastIntent.putExtra("DEVICE_INSTALL", true);
                broadcastIntent.putExtra("PIN", "0000");
                sendBroadcast(broadcastIntent);
            } else {
                getBinding().txtStatus.setText("Network is not connected.");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBinding().txtStatus.setText(isDMInstalled() ? "Device Manager is installed" : "Device Manager is not installed");
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean isDMInstalled() {
        try {
            getPackageManager().getApplicationInfo("com.aslam.dm", PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }
}