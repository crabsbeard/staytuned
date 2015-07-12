package com.aditya.staytuned.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.aditya.staytuned.HomeActivity;
import com.aditya.staytuned.MainActivity;
import com.aditya.staytuned.R;
import com.aditya.staytuned.pojo.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

/**
 * Created by devad_000 on 12-07-2015.
 * Service to show data from the database
 */
public class VisualDataService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{

    private static final String TAG = "com.aditya.staytuned.services";
    GoogleApiClient googleApiClient;
    Location lastKnowLocation;
    private String nearPlace;
    private final IBinder mBinder = new MyBinder();
    private double latitude;
    private double longitude;
    private boolean addressRequested;
    private AddressResultReceiver addressResultReceiver;
    private String addressOutput;
    PendingResult<PlaceLikelihoodBuffer> result;
    boolean requestingLocationUpdate = true;
    LocationRequest mLocationRequest;
    @Override
    public void onLocationChanged(Location location) {
        lastKnowLocation = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            addressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
        }

    }

    protected void createLocationRequest(){
         mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String username  = intent.getStringExtra("username.service");
        initApiClient();
        googleApiClient.connect();
        getPlaces();
        startNotification();
        return Service.START_STICKY;
    }

    private void getPlaces() {
        result = Places.PlaceDetectionApi
                .getCurrentPlace(googleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                nearPlace = likelyPlaces.get(0).getPlace().getName().toString();
                likelyPlaces.release();
            }
        });
    }

    protected synchronized void initApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        lastKnowLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastKnowLocation != null) {
            latitude = lastKnowLocation.getLatitude();
            longitude = lastKnowLocation.getLongitude();
            addressRequested = true;
            fetchLocationStarter(lastKnowLocation);
        } else {
            //will handle later
        }
        if(requestingLocationUpdate){
            startLocationUpdate();
        }
    }

    private void startLocationUpdate() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
    }


    private void fetchLocationStarter(Location lastKnowLocation) {
        if (!Geocoder.isPresent()) {
            //handle unavailability of geocoder
        }
        if (addressRequested) {
            startIntentService(lastKnowLocation);
        }
    }

    private void startIntentService(Location lastKnowLocation) {
        Intent intent = new Intent(this, GetAddressIntentService.class);
        addressResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, addressResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, lastKnowLocation);
        startService(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public class MyBinder extends Binder {

    }


    private void startNotification() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        RemoteViews mRemoteView = new RemoteViews(getPackageName(), R.layout.custom_notification);
        mRemoteView.setTextViewText(R.id.tv_title, "StayTuned");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setContent(mRemoteView);
        Notification mNotification = mBuilder.build();
        mNotification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
    }
}











