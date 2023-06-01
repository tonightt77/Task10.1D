package com.app.demo.service;



import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectionsApiClient {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    private DirectionsApiService service;

    public DirectionsApiClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(DirectionsApiService.class);
    }

    public void getDirections(String origin, String destination, String apiKey, Callback<DirectionsApiResponse> callback) {
        Call<DirectionsApiResponse> call = service.getDirections(origin, destination, "driving", apiKey);
        call.enqueue(callback);
    }
}
