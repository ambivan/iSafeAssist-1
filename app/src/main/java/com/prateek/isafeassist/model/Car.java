package com.prateek.isafeassist.model;

public class Car {

    String car;
    String carid;
    String carmake;
    String carmodel;
    String year;
    String regno;
    String insuranceco;
    String insuranceexp;

    public Car() {

    }

    public Car(String carid, String car, String carmake, String carmodel, String year, String regno, String insuranceco, String insuranceexp) {
        this.car = car;
        this.carmake = carmake;
        this.carmodel = carmodel;
        this.year = year;
        this.regno = regno;
        this.insuranceco = insuranceco;
        this.insuranceexp = insuranceexp;
        this.carid = carid;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
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
}
