package com.aslam.dmh;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onResume() {
        super.onResume();
        if (downloadStatus == DownloadingTask.ACTION_FAILED) {
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
                } else if (action == DownloadingTask.ACTION_COMPLETED) {
                    downloadStatus = action;
                    installDM(path);
                } else if (action == DownloadingTask.ACTION_FAILED) {
                    downloadStatus = action;
                    getBinding().txtStatus.setText("Downloading updates were failed. Please try again in few minutes.");
                    getBinding().progressBar.setVisibility(View.GONE);
                    // completeTask();
                }
            });
            downloadingTask.start(HelperBroadcastReceiver.URL, path);
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