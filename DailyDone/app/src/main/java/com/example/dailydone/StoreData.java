package com.example.dailydone;

public class StoreData {

    String emailObj;
    String phoneObj;

    public StoreData(){

    }

    public StoreData(String emailObj, String phoneObj) {
        this.emailObj = emailObj;
        this.phoneObj = phoneObj;
    }

    public String getEmailObj() {
        return emailObj;
    }

    public void setEmailObj(String emailObj) {
        this.emailObj = emailObj;
    }

    public String getPhoneObj() {
        return phoneObj;
    }

    public void setPhoneObj(String phoneObj) {
        this.phoneObj = phoneObj;
    }
}
