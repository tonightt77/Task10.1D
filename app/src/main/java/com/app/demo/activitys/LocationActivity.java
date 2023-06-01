package com.app.demo.activitys;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.app.demo.AppManager;
import com.app.demo.R;
import com.app.demo.beans.OrderBean;
import com.app.demo.service.CalculateTimeTask;
import com.app.demo.service.DirectionsApiClient;
import com.app.demo.service.DirectionsApiResponse;
import com.app.shop.mylibrary.base.BaseActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends BaseActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    @BindView(R.id.tv_book)
    TextView tv_book;
    @BindView(R.id.tv_call)
    TextView tv_call;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.money)
    TextView money;

    private OrderBean bean;
    private Bundle bundle;
    private LatLng geoPoint;
    private static final double DEGREES_TO_RADIANS = (180 / Math.PI);
    private final static double EARTHS_RADIUS[] = {6378.1, // Kilometers
            3963.1676, // Statue miles
            3443.89849 // Nautical miles
    };
    private String money1;
    private String pos;

    @OnClick({R.id.tv_book, R.id.tv_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_book:
                bundle.putParcelable("bean", bean);
                bundle.putString("money", money1);
                showActivity(LocationActivity.this, PayActivity.class, bundle);
                break;
            case R.id.tv_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + "13111111111");
                intent.setData(data);
                startActivity(intent);
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        bundle = getIntent().getExtras();
        bean = (OrderBean) bundle.getParcelable("bean");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private static double calclateArc(double aLat, double aLong, double bLat, double bLong) {
        double aLatRad = aLat / DEGREES_TO_RADIANS;
        double aLongRad = aLong / DEGREES_TO_RADIANS;
        double bLatRad = bLat / DEGREES_TO_RADIANS;
        double bLongRad = bLong / DEGREES_TO_RADIANS;

        double t1 = Math.cos(aLatRad) * Math.cos(aLongRad) * Math.cos(bLatRad) * Math.cos(bLongRad);
        double t2 = Math.cos(aLatRad) * Math.sin(aLongRad) * Math.cos(bLatRad) * Math.sin(bLongRad);
        double t3 = Math.sin(aLatRad) * Math.sin(bLatRad);
        double tt = Math.acos(t1 + t2 + t3);

        return tt;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geoPoint = getLocationInfo(bean.getLocation());
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        DrawLine(latLng);
        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
        double v = calclateArc(location.getLatitude(), location.getLongitude(), geoPoint.latitude, geoPoint.longitude) * EARTHS_RADIUS[0];
        money1 = String.format("%.2f", v * 5);
        money.setText("Approx. Fare: $" + money1);
        time.setText("Approx. Travel time: " + formatDateTime((long) Double.parseDouble(String.format("%.2f", v)) * 2 * 60));
//        getDirections(latLng, new LatLng(geoPoint.latitude,
//                geoPoint.longitude));
    }

    public String formatDateTime(long mss) {
        String DateTimes = null;
        long days = mss / (60 * 60 * 24);
        long hours = (mss % (60 * 60 * 24)) / (60 * 60);
        long minutes = (mss % (60 * 60)) / 60;
        long seconds = mss % 60;
        StringBuilder bud = new StringBuilder();
        if (days > 0) {
            bud.append(days + "d");
            if (hours > 0) {
                bud.append(hours + "h");
            }
            if (minutes > 0) {
                bud.append(minutes + "min");
            }
            if (seconds > 0) {
                bud.append(seconds + "s");
            }
            DateTimes = bud.toString();
        } else if (hours > 0) {
            bud.append(hours + "h");
            if (minutes > 0) {
                bud.append(minutes + "min");
            }
            if (seconds > 0) {
                bud.append(seconds + "s");
            }
            DateTimes = bud.toString();
        } else if (minutes > 0) {
            bud.append(minutes + "min");
            if (seconds > 0) {
                bud.append(seconds + "s");
            }
            DateTimes = bud.toString();
        } else {
            DateTimes = seconds + "s";
        }

        return DateTimes;
    }

    public void DrawLine(LatLng location) {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.add(location).add(new LatLng(geoPoint.latitude, geoPoint.longitude));
        mMap.addPolyline(polylineOptions);
    }

    public LatLng getLocationInfo(String address) {
        Geocoder geocoder = new Geocoder(LocationActivity.this, Locale.getDefault());
        LatLng firstLatLng = null;
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                double lat = addresses.get(0).getLatitude();
                double lng = addresses.get(0).getLongitude();

                LatLng latLng = new LatLng(lat, lng);
                if (firstLatLng == null) {
                    firstLatLng = latLng;
                }
                mMap.addMarker(new MarkerOptions().position(latLng).title(address));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return firstLatLng;
    }
}