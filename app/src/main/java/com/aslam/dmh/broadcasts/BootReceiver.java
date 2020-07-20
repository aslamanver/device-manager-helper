package com.aslam.dmh.broadcasts;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

    private static final int INTERVAL = 1000 * 10;
    Handler mHandler = new Handler();
    Runnable mRunnable;

    @Override
    public void onReceive(Context context, Intent intent) {
        // try {
        //     ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.aslam.dm", PackageManager.GET_META_DATA);
        //     PackageInfo packageInfo = context.getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
        // } catch (PackageManager.NameNotFoundException e) {
        //     if (isNetworkConnected(context)) {
        //         mRunnable = () -> {
        //             try {
        //                 ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo("com.aslam.dm", PackageManager.GET_META_DATA);
        //                 PackageInfo packageInfo = context.getPackageManager().getPackageInfo(applicationInfo.packageName, 0);
        //                 mHandler.removeCallbacks(mRunnable);
        //             } catch (PackageManager.NameNotFoundException ex) {
        //                 Intent broadcastIntent = new Intent("com.aslam.dmh.FROM_DM");
        //                 broadcastIntent.setComponent(new ComponentName("com.aslam.dmh", "com.aslam.dmh.broadcasts.HelperBroadcastReceiver"));
        //                 broadcastIntent.putExtra("DEVICE_INSTALL", true);
        //                 broadcastIntent.putExtra("PIN", "0000");
        //                 context.sendBroadcast(broadcastIntent);
        //                 mHandler.removeCallbacks(mRunnable);
        //                 mHandler.postDelayed(mRunnable, INTERVAL);
        //             }
        //         };
        //         mHandler.removeCallbacks(mRunnable);
        //         mHandler.post(mRunnable);
        //     }
        // }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }
}
