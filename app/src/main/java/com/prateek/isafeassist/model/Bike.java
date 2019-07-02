package com.prateek.isafeassist.model;

public class Bike {
    String bike;
    String bikemake;
    String bikemodel;
    String year;
    String regno;
    String insuranceco;
    String insuranceexp;
    public String firstname;
    public String lastname;
    public String email;
    public String mobno;
    public String address;
    public String landmark;
    public String zip;
    public String city;
    public String state;
    String bikeid;

    public String getBikeid() {
        return bikeid;
    }

    public void setBikeid(String bikeid) {
        this.bikeid = bikeid;
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

    public String getBike() {
        return bike;
    }

    public void setBike(String bike) {
        this.bike = bike;
    }

    public String getBikemake() {
        return bikemake;
    }

    public void setBikemake(String bikemake) {
        this.bikemake = bikemake;
    }

    public String getBikemodel() {
        return bikemodel;
    }

    public void setBikemodel(String bikemodel) {
        this.bikemodel = bikemodel;
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

    public Bike() {

    }

    public Bike(String bikeid, String bike, String bikemake, String bikemodel, String year, String regno, String insuranceco, String insuranceexp) {
        this.bike = bike;
        this.bikemake = bikemake;
        this.bikemodel = bikemodel;
        this.year = year;
        this.regno = regno;
        this.insuranceco = insuranceco;
        this.insuranceexp = insuranceexp;
        this.bikeid= bikeid;
    }

}
