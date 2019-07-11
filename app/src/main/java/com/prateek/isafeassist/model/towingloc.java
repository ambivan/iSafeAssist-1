package com.prateek.isafeassist.model;

public class towingloc {
    String startlat;
    String startlong;
    String endlat;
    String endlong;
    String distance;
    String requesting;
    String uname;
    String ucontact;
    String time;
    String endrequest;


    public towingloc(){

    }

    public towingloc(String startlat, String startlong, String endlat, String endlong) {
        this.startlat = startlat;
        this.startlong = startlong;
        this.endlat = endlat;
        this.endlong = endlong;
    }

    public String getEndrequest() {
        return endrequest;
    }

    public void setEndrequest(String endrequest) {
        this.endrequest = endrequest;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUcontact() {
        return ucontact;
    }

    public void setUcontact(String ucontact) {
        this.ucontact = ucontact;
    }

    public String getRequesting() {
        return requesting;
    }

    public void setRequesting(String requesting) {
        this.requesting = requesting;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStartlat() {
        return startlat;
    }

    public void setStartlat(String startlat) {
        this.startlat = startlat;
    }

    public String getStartlong() {
        return startlong;
    }

    public void setStartlong(String startlong) {
        this.startlong = startlong;
    }

    public String getEndlat() {
        return endlat;
    }

    public void setEndlat(String endlat) {
        this.endlat = endlat;
    }

    public String getEndlong() {
        return endlong;
    }

    public void setEndlong(String endlong) {
        this.endlong = endlong;
    }
}
