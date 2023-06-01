package com.app.demo.service;

import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class CalculateTimeTask extends AsyncTask<Void, Void, String> {
    private static final String TAG = "CalculateTimeTask";
    private LatLng origin;
    private LatLng destination;
    private String apiKey;

    public CalculateTimeTask(LatLng origin, LatLng destination, String apiKey) {
        this.origin = origin;
        this.destination = destination;
        this.apiKey = apiKey;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
            DirectionsResult directionsResult = DirectionsApi.newRequest(context)
                .mode(TravelMode.DRIVING)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
                .await();
            if (directionsResult != null && directionsResult.routes.length > 0) {
                return directionsResult.routes[0].legs[0].duration.humanReadable;
            }
        } catch (ApiException e) {
            Log.e(TAG, "Error getting directions", e);
        } catch (InterruptedException e) {
            Log.e(TAG, "Error getting directions", e);
        } catch (IOException e) {
            Log.e(TAG, "Error getting directions", e);
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "Driving time is " + result);
    }
}
