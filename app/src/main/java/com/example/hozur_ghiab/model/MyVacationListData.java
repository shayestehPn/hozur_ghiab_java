package com.example.hozur_ghiab.model;

import android.graphics.drawable.Drawable;

public class MyVacationListData {    //class for itemsof vacationReport recycclerView items
    private String day;
    private Drawable typeBackground;
    public String status;
    public String title;
    public String description;
    public String details;
    public String createdAt;


    public MyVacationListData(){

    }



    public String getDay() {
        return day;
    }
    public void setDay(String myDay) {
        this.day = myDay; }


    public Drawable getTypeBackground() {
        return typeBackground;
    }
    public void setTypeBackground(Drawable myTypeBackground) {
        this.typeBackground=myTypeBackground;
    }


    public String getStatus() {
        return status;
    }
    public void setStatus(String myStatus) {
        this.status=myStatus;
    }


    public String getDescription() {
        return description;
    }
    public void setDescription(String myDescription) {
        this.description = myDescription;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String myTitle) {
        this.title = myTitle;
    }


    public String getDetails() {
        return details;
    }
    public void setDetails(String myDetails) {
        this.details=myDetails;
    }


    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String myCreatedAt) {
        this.createdAt=myCreatedAt;
    }

}

