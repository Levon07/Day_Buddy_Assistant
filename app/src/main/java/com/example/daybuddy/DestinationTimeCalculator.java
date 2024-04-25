package com.example.daybuddy;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DestinationTimeCalculator extends AsyncTask<Void, Void, String> {
    private static final String TAG = "DestinationTimeCalculator";

    private LatLng origin;
    private LatLng destination;
    private String apiKey = "AIzaSyBa-ttElPoQgDetwieuuMp360EJeXlr5RY";

    public DestinationTimeCalculator(LatLng origin, LatLng destination) {
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    protected String doInBackground(Void... voids) {
        String response = "";
        try {
            String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json" +
                    "?origins=" + origin.latitude + "," + origin.longitude +
                    "&destinations=" + destination.latitude + "," + destination.longitude +
                    "&key=" + apiKey;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            response = stringBuilder.toString();
        } catch (IOException e) {
            Log.e(TAG, "Error fetching data: " + e.getMessage());
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        if (response != null) {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                JSONArray rows = jsonResponse.getJSONArray("rows");
                if (rows.length() > 0) {
                    JSONObject row = rows.getJSONObject(0);
                    JSONArray elements = row.getJSONArray("elements");
                    if (elements.length() > 0) {
                        JSONObject element = elements.getJSONObject(0);
                        JSONObject duration = element.getJSONObject("duration");
                        String durationText = duration.getString("text");
                        // Use durationText as the estimated travel time
                    }
                }
            } catch (JSONException e) {
                Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            }
        }
    }
}
