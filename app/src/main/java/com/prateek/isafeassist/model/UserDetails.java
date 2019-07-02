package com.prateek.isafeassist.model;

public class UserDetails {
    String name;
    String email;
    String password;
    String contactNo;
    String signupid;

    public String getSignupid() {
        return signupid;
    }

    public void setSignupid(String signupid) {
        this.signupid = signupid;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public UserDetails() {

    }

    public UserDetails(String name, String email, String password, String contactNo, String signupid) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.contactNo = contactNo;
        this.signupid = signupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
