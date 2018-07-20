package cheemala.reqgeofence.com.utils;

import java.util.HashMap;

/**
 * Created by CheemalaCh on 7/20/2018.
 */

public class GeoFenceConstants {
    public static final String GEOFENCE_ID_SE = "SHIVAM_ELITE";
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;

    /**
     * Map for storing information about stanford university in the Stanford.
     */
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {
        // stanford university.
        AREA_LANDMARKS.put(GEOFENCE_ID_SE, new LatLng(17.364992, 78.3963182));
    }
}
