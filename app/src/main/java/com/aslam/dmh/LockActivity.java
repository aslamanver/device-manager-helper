package com.aslam.dmh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.aslam.dmh.databinding.ActivityLockBinding;

public class LockActivity extends BaseActivity<ActivityLockBinding> {

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("DEVICE_LOCKED")) {
                boolean isLocked = intent.getBooleanExtra("DEVICE_LOCKED", false);
                try {
                    if (!isLocked) {
                        stopLockTask();
                        finishAffinity();
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding(R.layout.activity_lock);
        try {
            startLockTask();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        IntentFilter intentFilter = new IntentFilter("com.aslam.dmhelper.FROM_DM");
        registerReceiver(broadcastReceiver, intentFilter);

        getBinding().btnUnlock.setOnClickListener(v -> {
            try {
                stopLockTask();
            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

}