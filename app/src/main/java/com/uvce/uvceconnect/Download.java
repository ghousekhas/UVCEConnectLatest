package com.uvce.uvceconnect;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;

public class Download {
    private Context context;
    private String fileName, url;

    Download(Context _context, String _fileName, String _url) {
        context = _context;
        fileName = _fileName;
        url = _url;
    }

    // execute the AsyncTask to download Files.
    public void start() {
        new downloadSyllabus().execute(url);
    }

    // AsyncTask which will take care of the download using, download manager.
    private class downloadSyllabus extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(context, "Downloading...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(String... url) {
            File directory = new File(Environment.getExternalStorageDirectory() + "/UVCE-Connect");

            if (!directory.exists()) {
                directory.mkdirs();
            }

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url[0]));
            request.setDescription("File Downloading....")
                    .setTitle(fileName)
                    .setNotificationVisibility(DownloadManager.Request
                            .VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir("/UVCE-Connect", fileName)
                    .allowScanningByMediaScanner();

            DownloadManager manager = (DownloadManager)
                    context.getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
            return null;
        }
    }

}
