package com.example.hozur_ghiab.model;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private String date;
    String numberShamsiDate;
    private String hollidayDescription="" ;
    private List<PieceOfTime> workTimes=new ArrayList<PieceOfTime>();
    private List<PieceOfTime> vacations=new ArrayList<PieceOfTime>();

    public Item(){

    }



    public void setDate(String myDate){
        this.date=myDate;
    }
    public String getDate() {
        return this.date;
    }

    public void setNumberShamsiDate(String myDate){
        this.numberShamsiDate=myDate;
    }
    public String getNumberShamsiDate() {
        return this.numberShamsiDate;
    }

    public void setHollidayDescription(String myHolliday){
        this.hollidayDescription=myHolliday;
    }
    public String getHollidayDescription(){
        return this.hollidayDescription;
    }


    public void addToWorkTimes(PieceOfTime myWorkTime){
        this.workTimes.add(myWorkTime);
    }
    public List<PieceOfTime> getWorkTimes(){
        return this.workTimes;
    }



    public void addToVacations(PieceOfTime myVacation){
        this.vacations.add(myVacation);
    }
    public List<PieceOfTime> getVacations(){
        return this.vacations;
    }


}
