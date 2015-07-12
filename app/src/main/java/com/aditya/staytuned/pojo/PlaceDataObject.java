package com.aditya.staytuned.pojo;

/**
 * Created by devad_000 on 12-07-2015.
 */
public class PlaceDataObject {
    private int id;
    private String username;
    private String placeName;
    private String locationName;
    private double lat;
    private double lon;
    private long timeStamp;

    public PlaceDataObject(int id, String username, String placeName, double lat, double lon, String locationName, long timeStamp) {
        this.id = id;
        this.username = username;
        this.placeName = placeName;
        this.locationName = locationName;
        this.lat = lat;
        this.lon = lon;
        this.timeStamp = timeStamp;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUsername() {
        return username;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getLocationName() {
        return locationName;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
