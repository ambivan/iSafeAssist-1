package com.prateek.isafeassist.payments.paymentdao;

public class PayDao {
    String transactid;
    String purchasedate;
    String expirydate;
    String membershipid;


    public PayDao(){

    }
    public PayDao(String transactid, String purchasedate, String expirydate, String membershipid) {
        this.transactid = transactid;
        this.purchasedate = purchasedate;
        this.expirydate = expirydate;
        this.membershipid= membershipid;
    }

    public String getMembershipid() {
        return membershipid;
    }

    public void setMembershipid(String membershipid) {
        this.membershipid = membershipid;
    }

    public String getTransactid() {
        return transactid;
    }

    public void setTransactid(String transactid) {
        this.transactid = transactid;
    }

    public String getPurchasedate() {
        return purchasedate;
    }

    public void setPurchasedate(String purchasedate) {
        this.purchasedate = purchasedate;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }
}
