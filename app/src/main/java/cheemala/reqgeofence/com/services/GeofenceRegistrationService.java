package cheemala.reqgeofence.com.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import cheemala.reqgeofence.com.utils.GeoFenceConstants;

/**
 * Created by CheemalaCh on 7/20/2018.
 */

public class GeofenceRegistrationService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private String TAG = "Geo_Service_";

    public GeofenceRegistrationService(String name) {
        super(name);
    }

    public GeofenceRegistrationService() {
        super("Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "GeofencingEvent error " + geofencingEvent.getErrorCode());
            broadCastGeofenceMsg("cheemala.reqgeofence.com.geofence.EVENT.SOMETHING_ERROR");
        } else {
            int transaction = geofencingEvent.getGeofenceTransition();
            List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
            Geofence geofence = geofences.get(0);
            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER && geofence.getRequestId().equals(GeoFenceConstants.GEOFENCE_ID_SE)) {
                Log.d(TAG, "You are near Shivam Elite");
                broadCastGeofenceMsg("cheemala.reqgeofence.com.geofence.EVENT.ENTER_TRIGGERED");
            } else if (transaction == Geofence.GEOFENCE_TRANSITION_EXIT && geofence.getRequestId().equals(GeoFenceConstants.GEOFENCE_ID_SE)) {
                Log.d(TAG, "You are out of Shivam Elite");
                broadCastGeofenceMsg("cheemala.reqgeofence.com.geofence.EVENT.EXIT_TRIGGERED");
            } else if (transaction == Geofence.GEOFENCE_TRANSITION_DWELL && geofence.getRequestId().equals(GeoFenceConstants.GEOFENCE_ID_SE)) {
                Log.d(TAG, "You are in Shivam Elite for 5min");
                broadCastGeofenceMsg("cheemala.reqgeofence.com.geofence.EVENT.EXIT_DWELL");
            } else {
            }
        }
    }

    private void broadCastGeofenceMsg(String geoFenceAction) {
        Intent broadCastEnterTrsnsition = new Intent();
        broadCastEnterTrsnsition.setAction(geoFenceAction);
        sendBroadcast(broadCastEnterTrsnsition);
    }
}
