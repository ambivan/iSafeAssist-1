package com.prateek.isafeassist.model;

public class membermodel {

    String purchased;
    String expiry;
    String transid;
    String mid;

    public membermodel(){

    }
    public membermodel(String purchased, String expiry, String transid, String mid) {
        this.purchased = purchased;
        this.expiry = expiry;
        this.transid = transid;
        this.mid= mid;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getPurchased() {
        return purchased;
    }

    public void setPurchased(String purchased) {
        this.purchased = purchased;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getAvailed() {
        return transid;
    }

    public void setAvailed(String transid) {
        this.transid = transid;
    }
}
