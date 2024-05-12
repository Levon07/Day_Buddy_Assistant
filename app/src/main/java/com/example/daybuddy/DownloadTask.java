package com.example.daybuddy;

public interface DownloadTask {
    String doInBackground(String... url);

    void onPostExecute(String result);
}
