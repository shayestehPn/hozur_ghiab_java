package com.example.hozur_ghiab.model;

public  class MyListData {     //class for dashboard reports recyclerView items
    private String enterTime;
    private String exitTime;
    public String description;

    public MyListData(String myEnterTime,String myExitTime,String myDescription){
        enterTime=myEnterTime;
        exitTime=myExitTime;
        description=myDescription;
    }
    public MyListData(){
        enterTime="...";
        exitTime="...";
        description="...";
    }

    public String getDescription() {
        return description; }                   //because description is private
    public void setDescription(String description) {
        this.description = description; }   //because description is private

    public String getEnterTime() {     //because enterTime is private
        return enterTime;
    }
    public void setEnterTime(String enterTime) {     //because enterTime is private
        this.enterTime=enterTime;
    }

    public String getExitTime() {     //because exitTime is private
        return exitTime;
    }
    public void setExitTime(String exitTime) {     //because exitTime is private
        this.exitTime=exitTime;
    }
}

