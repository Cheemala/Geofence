package cheemala.reqgeofence.com.utils;

/**
 * Created by CheemalaCh on 7/20/2018.
 */

public class LatLng {
    private double lat, longi;

    public LatLng(double lat, double longi) {
        this.lat = lat;
        this.longi = longi;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }
}
