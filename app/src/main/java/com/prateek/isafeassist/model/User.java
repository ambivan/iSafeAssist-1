package com.prateek.isafeassist.model;

public class User {
    public String firstname;
    public String lastname;
    public String email;
    public String mobno;
    public String address;
    public String landmark;
    public String zip;
    public String city;
    public String state;
    String car="";
    String carmake="";
    String carmodel="";
    String year;
    String regno="";
    String insuranceco="";
    String insuranceexp="";
    String cid;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getCarmake() {
        return carmake;
    }

    public void setCarmake(String carmake) {
        this.carmake = carmake;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getInsuranceco() {
        return insuranceco;
    }

    public void setInsuranceco(String insuranceco) {
        this.insuranceco = insuranceco;
    }

    public String getInsuranceexp() {
        return insuranceexp;
    }

    public void setInsuranceexp(String insuranceexp) {
        this.insuranceexp = insuranceexp;
    }

    public User() {

    }

    public User(String cid,String firstname, String lastname, String email, String mobno, String address, String landmark, String zip, String car, String carmake, String carmodel, String year, String regno, String insuranceco, String insuranceexp) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.mobno = mobno;
        this.address = address;
        this.landmark = landmark;
        this.zip = zip;
        this.cid= cid;
        this.car = car;
        this.carmake = carmake;
        this.carmodel = carmodel;
        this.year = year;
        this.regno = regno;
        this.insuranceco = insuranceco;
        this.insuranceexp = insuranceexp;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
