package com.example.dailydone;

public class StoreData {

    String phoneObj;
    String nameObj;

    public StoreData(){

    }

    public StoreData(String phoneObj, String nameObj) {
        this.phoneObj = phoneObj;
        this.nameObj = nameObj;
    }

    public String getPhoneObj() {
        return phoneObj;
    }

    public void setPhoneObj(String phoneObj) {
        this.phoneObj = phoneObj;
    }

    public String getNameObj() {
        return nameObj;
    }

    public void setNameObj(String nameObj) {
        this.nameObj = nameObj;
    }
}
