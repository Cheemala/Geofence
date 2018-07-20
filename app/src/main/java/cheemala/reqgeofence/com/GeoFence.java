package cheemala.reqgeofence.com;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import cheemala.reqgeofence.com.services.GeofenceRegistrationService;
import cheemala.reqgeofence.com.utils.GeoFenceConstants;
import cheemala.reqgeofence.com.utils.LatLng;
import cheemala.reqgeofence.com.utils.ShowMsg;

public class GeoFence extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final int LOCATION_REQ_CODE = 111;
    private GoogleApiClient googleApiClient = null;
    private PendingIntent pendingIntent;
    private BroadcastReceiver broadcastReceiver;
    private LocationCallback locationCallback;
    private String TAG = "Geo_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_fence);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                ShowMsg.showShortToast(GeoFence.this, locationResult.getLastLocation().getLatitude() + " , " + locationResult.getLastLocation().getLongitude());
            }
        };

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().contentEquals("cheemala.reqgeofence.com.geofence.EVENT.ENTER_TRIGGERED")) {
                    ShowMsg.showLongToast(GeoFence.this, "You entered location now!");
                } else if (intent.getAction().contentEquals("cheemala.reqgeofence.com.geofence.EVENT.EXIT_TRIGGERED")) {
                    ShowMsg.showLongToast(GeoFence.this, "You exited location now!");
                } else {
                    ShowMsg.showLongToast(GeoFence.this, "Something broadcasted !");
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cheemala.reqgeofence.com.geofence.EVENT.ENTER_TRIGGERED");
        intentFilter.addAction("cheemala.reqgeofence.com.geofence.EVENT.EXIT_TRIGGERED");
        this.registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkForPlayServices()) {
            checkForLocationPermissions();
        } else {
            ShowMsg.showShortToast(this, "Play services is un-availbale!");
        }
    }

    private void checkForLocationPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GeoFence.this, new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_REQ_CODE);

            } else {
                initialiseGoogleApiClient();
            }
        } else {
            initialiseGoogleApiClient();
        }

    }

    private void initialiseGoogleApiClient() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            if (googleApiClient != null) {
                googleApiClient.reconnect();
            }
        } else {
            googleApiClient.reconnect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean checkForPlayServices() {
        return GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG + "" + permissions[0] + " ", "_granted");
                initialiseGoogleApiClient();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG + " Google api clinet!", "_connected!");
        startAccessingLocations();
        startMonitoringGeofences();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG + " Google api clinet!", " connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG + " Google api clinet!", " " + connectionResult.getErrorMessage());
    }

    private void startAccessingLocations() {

        Log.d(TAG + " Location_Services_", "start location monitor");
        final LocationRequest locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG + " Location_Services_", "not granted!");
            return;
        }
        LocationServices.getFusedLocationProviderClient(GeoFence.this).requestLocationUpdates(locationRequest, locationCallback, null);

    }

    private void startMonitoringGeofences() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getGeofencingClient(GeoFence.this).addGeofences(getGeofencingRequest(), getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("" + TAG, "Gefence con successfully connected!");
                } else {
                    Log.d("" + TAG, "Gefence con something went wrong!");
                }

            }
        });

    }

    @NonNull
    private Geofence getGeofence() {
        LatLng latLng = GeoFenceConstants.AREA_LANDMARKS.get(GeoFenceConstants.GEOFENCE_ID_SE);
        return new Geofence.Builder()
                .setRequestId(GeoFenceConstants.GEOFENCE_ID_SE)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setCircularRegion(latLng.getLat(), latLng.getLongi(), GeoFenceConstants.GEOFENCE_RADIUS_IN_METERS)
                .setNotificationResponsiveness(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setLoiteringDelay(100)
                .build();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofence(getGeofence());
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceRegistrationService.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopMonitoringGeofenceStuff();
        unRegisterBRecievers();
        if (googleApiClient.isConnected())
            googleApiClient.disconnect();
    }

    private void unRegisterBRecievers() {
        this.unregisterReceiver(broadcastReceiver);
    }

    private void stopMonitoringGeofenceStuff() {
        LocationServices.getGeofencingClient(GeoFence.this).removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                    Log.d("" + TAG, "_stopped listening to geofence!");
            }
        });

        LocationServices.getFusedLocationProviderClient(GeoFence.this).removeLocationUpdates(locationCallback);
    }
}
