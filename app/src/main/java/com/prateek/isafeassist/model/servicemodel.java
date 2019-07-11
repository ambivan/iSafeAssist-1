package com.prateek.isafeassist.model;

public class servicemodel {
    String dname;
    String dcontact;
    String dotp;
    String price;

    public servicemodel(String dname, String dcontact) {
        this.dname = dname;
        this.dcontact = dcontact;
    }

    public servicemodel(){

    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDotp() {
        return dotp;
    }

    public void setDotp(String dotp) {
        this.dotp = dotp;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getDcontact() {
        return dcontact;
    }

    public void setDcontact(String dcontact) {
        this.dcontact = dcontact;
    }
}
