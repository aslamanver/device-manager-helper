package com.aslam.dmh.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.aslam.dmh.LockActivity;
import com.aslam.dmh.OperationActivity;

public class HelperBroadcastReceiver extends BroadcastReceiver {

    public static final String URL = "https://cdn.payable.lk/apks/dm.apk";
    public static String PATH = null;
    public static String PIN = "0000";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.hasExtra("DEVICE_LOCKED")) {

            boolean isLocked = intent.getBooleanExtra("DEVICE_LOCKED", false);
            if (isLocked) {
                Intent in = new Intent(context, LockActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(in);
            }

        } else if (intent.hasExtra("DEVICE_UPDATE")) {

            PATH = intent.getStringExtra("PATH");
            PIN = intent.getStringExtra("PIN");

            Intent in = new Intent(context, OperationActivity.class);
            in.putExtra("DEVICE_UPDATE", true);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);

        } else if (intent.hasExtra("DEVICE_INSTALL")) {

            PIN = intent.getStringExtra("PIN");

            Intent in = new Intent(context, OperationActivity.class);
            in.putExtra("DEVICE_INSTALL", true);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
        }
    }
}
