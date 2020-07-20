package com.aslam.dmh.providers;

import android.content.Context;
import android.os.AsyncTask;

public class ApplicationTasks extends AsyncTask<String, Integer, Integer> {

    public static final int ACTION_STARTED = 1;
    public static final int ACTION_COMPLETED = 3;
    public static final int ACTION_FAILED = 4;

    public static final String TASK_INSTALL = "task_install";
    public static final String TASK_UNINSTALL = "task_uninstall";

    public interface Listener {
        void onAction(int action);
    }

    private Listener listener;
    private Context context;

    public ApplicationTasks(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(String... strings) {

        if (strings[0].equals(TASK_INSTALL)) {
            return DeviceOSUtils.silentInstall(strings[1]) ? ACTION_COMPLETED : ACTION_FAILED;
        }

        if (strings[0].equals(TASK_UNINSTALL)) {
            return DeviceOSUtils.silentUninstall(strings[1]) ? ACTION_COMPLETED : ACTION_FAILED;
        }

        return ACTION_FAILED;
    }

    public void start(String task, String value) {
        execute(task, value);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onAction(ACTION_STARTED);
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        listener.onAction(result);
    }
}
