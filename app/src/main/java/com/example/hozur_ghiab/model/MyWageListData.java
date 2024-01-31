package com.example.hozur_ghiab.model;

import android.graphics.drawable.Drawable;

public class MyWageListData {

    private Drawable statusBackground;
    public String type,value,description,day,status,createdAt;

    public MyWageListData(){
    }

    public String getDay() {
        return day;
    }
    public void setDay(String myDay) {
        this.day = myDay; }


    public String getStatus() {
        return status;
    }
    public void setStatus(String myStatus) {
        this.status = myStatus; }

    public Drawable getstatusBackground() {
        return statusBackground;
    }
    public void setStatusBackground(Drawable myStatusBackground) {
        this.statusBackground=myStatusBackground;
    }


    public String getType() {
        return type;
    }
    public void setType(String myType) {
        this.type=myType;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String myDescription) {
        this.description = myDescription;
    }


    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String myCreatedAt) {
        this.createdAt=myCreatedAt;
    }


    public String getValue() {
        return value;
    }
    public void setValue(String myValue) {
        this.value=myValue;
    }

}
