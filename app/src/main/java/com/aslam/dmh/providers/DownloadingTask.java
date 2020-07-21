package com.aslam.dmh.providers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DownloadingTask extends AsyncTask<String, Integer, Integer> {

    public static final int ACTION_STARTED = 1;
    public static final int ACTION_PROGRESS = 2;
    public static final int ACTION_COMPLETED = 3;
    public static final int ACTION_FAILED = 4;

    public interface Listener {
        void onProgress(int action, int progress, Exception ex);
    }

    private Context context;
    private Listener listener;
    private Exception exception;
    private ProgressDialog progressDialog;
    private boolean isProgressDialog;
    private int progress = 0;

    public DownloadingTask(Context context, boolean isProgressDialog, Listener listener) {
        construct(context, isProgressDialog, listener);
    }

    public DownloadingTask(Context context, Listener listener) {
        construct(context, true, listener);
    }

    public DownloadingTask(Context context) {
        construct(context, true, (action, progress, ex) -> {
            return;
        });
    }

    private void construct(Context context, boolean isProgressDialog, Listener listener) {
        this.context = context;
        this.isProgressDialog = isProgressDialog;
        this.listener = listener;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Downloading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
    }

    @Override
    protected Integer doInBackground(String... strings) {

        try {

            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(
                                java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());


            URL url = new URL(strings[0]);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(1000 * 30);
            connection.setReadTimeout(1000 * 30);
            connection.connect();
            int fileLength = connection.getContentLength();
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(strings[1]);
            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                int progress = (int) (total * 100 / fileLength);
                publishProgress(progress);
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();

        } catch (Exception ex) {
            this.exception = ex;
            return ACTION_FAILED;
        }

        return ACTION_COMPLETED;
    }

    public void start(String url, String path) {
        execute(url, path);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isProgressDialog) {
            progressDialog.setProgress(0);
            progressDialog.show();
        }
        listener.onProgress(ACTION_STARTED, 0, exception);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (progress != values[0]) {
            progress = values[0];
            progressDialog.setProgress(values[0]);
            listener.onProgress(ACTION_PROGRESS, values[0], exception);
        }
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        listener.onProgress(result, 100, exception);
    }
}
