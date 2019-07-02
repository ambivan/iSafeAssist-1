package com.prateek.isafeassist.driverdetails.dao;

public class DriverDetails {
    String dname;
    String dnumber;
    String dsublocality;
    Long dlatitude;
    Long dlongitude;
    String dstate;

    public DriverDetails(){

    }

    public DriverDetails(String dname, String dnumber, String dsublocality, Long dlatitude, Long dlongitude, String dstate) {
        this.dname = dname;
        this.dnumber = dnumber;
        this.dsublocality = dsublocality;
        this.dlatitude = dlatitude;
        this.dlongitude = dlongitude;
        this.dstate = dstate;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDnumber() {
        return dnumber;
    }

    public void setDnumber(String dnumber) {
        this.dnumber = dnumber;
    }

    public String getDsublocality() {
        return dsublocality;
    }

    public void setDsublocality(String dsublocality) {
        this.dsublocality = dsublocality;
    }

    public Long getDlatitude() {
        return dlatitude;
    }

    public void setDlatitude(Long dlatitude) {
        this.dlatitude = dlatitude;
    }

    public Long getDlongitude() {
        return dlongitude;
    }

    public void setDlongitude(Long dlongitude) {
        this.dlongitude = dlongitude;
    }

    public String getDstate() {
        return dstate;
    }

    public void setDstate(String dstate) {
        this.dstate = dstate;
    }
}
