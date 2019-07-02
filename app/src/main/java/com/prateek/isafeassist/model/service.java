package com.prateek.isafeassist.model;

public class service {

    String servicename;
    String price;


    public service(){

    }
    public service(String servicename, String price) {
        this.servicename = servicename;
        this.price = price;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
