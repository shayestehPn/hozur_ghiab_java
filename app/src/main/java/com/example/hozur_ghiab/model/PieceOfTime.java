package com.example.hozur_ghiab.model;

public class PieceOfTime {

        private String date;
        private String start;
        private String end;
        private String description;

        public PieceOfTime(){
        };


        public void setStart(String myStart){
        this.start=myStart;
    }

        public String getStart(){
            return this.start;
        }



        public void setEnd(String myEnd){
        this.end=myEnd;
         }

        public String getEnd(){
            return this.end;
        }



        public void setDescription(String myDescription){
        this.description=myDescription;
        }

        public String getDescription(){
            return this.description;
        }








    }
