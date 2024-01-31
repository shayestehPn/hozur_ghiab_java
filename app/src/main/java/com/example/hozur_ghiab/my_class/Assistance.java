package com.example.hozur_ghiab.my_class;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.hozur_ghiab.my_class.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class Assistance {

    public static String convert(String number){           //to convert MobileNumber from persian to english
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }

    //to check if phone number is valid
    public static Boolean isValid(String phoneNumber){
        Boolean b;
        if(Assistance.convert(phoneNumber).matches("(\\+98|0)?9\\d{9}")) b=true;
        else b=false;
        return b;
    }

    public static String compareMiladiDates(String d1,String d2) throws ParseException     //to compare date1 and date2
    {
        String state="equal";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(d1);
        Date date2 = sdf.parse(d2);

        int result = date1.compareTo(date2);
        if (result == 0) {
            state="equal";     //to return equal if two dates are equal
        } else if (result > 0) {
            state="after";     //to return after if date1 is after date2
        } else if (result < 0) {
            state="before";    //to return before if date1 is before date2
        }
        return state;
    }

    public static String compareShamsiDates(String myDate1,String myDate2) throws ParseException     //to compare date1 and date2
    {
        String state="equal";
        String d1=convertDateToMiladi(myDate1);
        String d2=convertDateToMiladi(myDate2);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = sdf.parse(d1);
        Date date2 = sdf.parse(d2);

        int result = date1.compareTo(date2);
        if (result == 0) {
            state="equal";     //to return equal if two dates are equal
        } else if (result > 0) {
            state="after";     //to return after if date1 is after date2
        } else if (result < 0) {
            state="before";    //to return before if date1 is before date2
        }
        return state;
    }

    public static String convertDateToShamsi(String date){    //to convert date from   miladi to shamsi
        String year=date.substring(0,4);
        String month=date.substring(5,7);
        String day=date.substring(8);
        Roozh jCal = new Roozh();
        jCal.GregorianToPersian(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
        return jCal.toString();
    }

    public static String convertDateToShamsi(String year,String month,String day){    //to convert date from  miladi to shamsi
        Roozh jCal = new Roozh();
        jCal.GregorianToPersian(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
        return jCal.toString();
    }

    public static String convertDateToMiladi(int Year, int Month, int Day){    //to convert date from shamsi to miladi
        Roozh jCal = new Roozh();
        jCal.PersianToGregorian(Year,Month,Day);
        return jCal.toString();
    }

    public static String convertDateToMiladi(String date){    //to convert date from shamsi to miladi
        int year=Integer.parseInt(date.substring(0,4));
        int month=Integer.parseInt(date.substring(5,7));
        int day=Integer.parseInt(date.substring(8));
        Roozh jCal = new Roozh();
        jCal.PersianToGregorian(year,month,day);
        return jCal.toString();
    }


    public static String defineMonthName(int month){    //to define  persian month title by its value
        String result=null;
        switch (month){
            case 1:
                result="فروردین";
                break;
            case 2:
                result="اردیبهشت";
                break;
            case 3:
                result="خرداد";
                break;
            case 4:
                result="تیر";
                break;
            case 5:
                result="مرداد";
                break;
            case 6:
                result="شهریور";
                break;
            case 7:
                result="مهر";
                break;
            case 8:
                result="آیان";
                break;
            case 9:
                result="آذر";
                break;
            case 10:
                result="دی";
                break;
            case 11:
                result="بهمن";
                break;
            case 12:
                result="اسفند";
                break;
        }
        return result;
    }



    public static String defineMonthName(String myMonth){  //to define  persian month title by its value
        int month=Integer.parseInt(myMonth);
        String result=null;
        switch (month){
            case 1:
                result="فروردین";
                break;
            case 2:
                result="اردیبهشت";
                break;
            case 3:
                result="خرداد";
                break;
            case 4:
                result="تیر";
                break;
            case 5:
                result="مرداد";
                break;
            case 6:
                result="شهریور";
                break;
            case 7:
                result="مهر";
                break;
            case 8:
                result="آبان";
                break;
            case 9:
                result="آذر";
                break;
            case 10:
                result="دی";
                break;
            case 11:
                result="بهمن";
                break;
            case 12:
                result="اسفند";
                break;
        }
        return result;
    }

    public static String removeZero(String dayValue){  //to remove zero from first of the String
        String result=dayValue;
        if (dayValue.substring(0,1).equals("0"))
            result=result.substring(1);
        return result;
    }


}
