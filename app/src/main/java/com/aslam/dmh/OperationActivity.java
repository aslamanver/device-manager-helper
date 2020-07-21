package com.aslam.dmh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.aslam.dmh.broadcasts.HelperBroadcastReceiver;
import com.aslam.dmh.databinding.ActivityOperationBinding;
import com.aslam.dmh.providers.ApplicationTasks;
import com.aslam.dmh.providers.DownloadingTask;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class OperationActivity extends BaseActivity<ActivityOperationBinding> {

    int downloadStatus = 0;
    Handler downloadWatchHandler = new Handler();
    Runnable downloadWatchRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBinding(R.layout.activity_operation);

        getBinding().imgLogo.setOnClickListener(v -> {
            completeTask();
        });

        if (getIntent().hasExtra("DEVICE_UPDATE")) {
            screenLock();
            installDM(HelperBroadcastReceiver.PATH);
        }

        if (getIntent().hasExtra("DEVICE_INSTALL")) {
            downloadDM();
        }
    }

    private void downloadDM() {
        try {
            URI uri = new URI(HelperBroadcastReceiver.URL);
            String fileName = new File(uri.getPath()).getName();
            String path = getCacheDir().getAbsolutePath() + "/" + fileName;
            DownloadingTask downloadingTask = new DownloadingTask(this, false, (action, progress, ex) -> {
                if (action == DownloadingTask.ACTION_STARTED) {
                    downloadStatus = action;
                    getBinding().progressBar.setVisibility(View.VISIBLE);
                    getBinding().txtStatus.setText("Downloading updates started.");
                } else if (action == DownloadingTask.ACTION_PROGRESS) {
                    downloadStatus = action;
                    getBinding().progressBar.setVisibility(View.VISIBLE);
                    getBinding().txtStatus.setText("Downloading " + progress + "%");
                } else if (action == DownloadingTask.ACTION_COMPLETED) {
                    downloadStatus = action;
                    getBinding().progressBar.setVisibility(View.VISIBLE);
                    getBinding().txtStatus.setText("Installing application...");
                    installDM(path);
                } else if (action == DownloadingTask.ACTION_FAILED) {
                    downloadStatus = action;
                    getBinding().txtStatus.setText("Downloading updates were failed. Please try again in few minutes.");
                    getBinding().progressBar.setVisibility(View.GONE);
                    completeTask();
                }
            });
            downloadingTask.start(HelperBroadcastReceiver.URL, path);
            downloadWatchHandler.removeCallbacks(downloadWatchRunnable);
            downloadWatchRunnable = () -> {
                if (downloadStatus == DownloadingTask.ACTION_STARTED || downloadStatus == DownloadingTask.ACTION_PROGRESS) {
                    downloadStatus = DownloadingTask.ACTION_FAILED;
                    getBinding().txtStatus.setText("Downloading timeout. Please try again in few minutes.");
                    getBinding().progressBar.setVisibility(View.GONE);
                    downloadingTask.cancel(true);
                }
            };
            downloadWatchHandler.postDelayed(downloadWatchRunnable, 1000 * 60 * 1);
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private void installDM(String path) {
        ApplicationTasks applicationTasks = new ApplicationTasks(this, action -> {
            if (action == ApplicationTasks.ACTION_COMPLETED || action == ApplicationTasks.ACTION_FAILED) {
                completeTask();
                Toast.makeText(getApplicationContext(), action == ApplicationTasks.ACTION_COMPLETED ? "UPDATE_COMPLETED" : "UPDATE_FAILED", Toast.LENGTH_LONG).show();
                Intent intent = getPackageManager().getLaunchIntentForPackage("com.aslam.dm");
                startActivity(intent);
            }
        });
        applicationTasks.start(ApplicationTasks.TASK_INSTALL, path);
    }

    private void completeTask() {
        try {
            finish();
            stopLockTask();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void screenLock() {
        try {
            startLockTask();
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (downloadStatus == DownloadingTask.ACTION_FAILED) {
            super.onBackPressed();
        }
    }
}