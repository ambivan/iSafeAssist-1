package com.prateek.isafeassist.model;

public class UserLocationService {
    String latitude;
    String longitude;

    public UserLocationService(){

    }

    public UserLocationService(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
