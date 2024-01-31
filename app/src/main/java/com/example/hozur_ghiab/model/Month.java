package com.example.hozur_ghiab.model;

public class Month {    //class for months in vacationReport
    public int value;
    public String title;

    public Month(String myTitle, int myValue){
        value=myValue;
        title=myTitle;
    }

    public Month(){
    }


    public int getValue() {
        return value;
    }
    public void setValue(int myValue) {
        this.value=myValue;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String myTitle) {
        this.title=myTitle;
    }

}
