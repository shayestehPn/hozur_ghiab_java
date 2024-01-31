package com.example.hozur_ghiab.view_model;

import static com.example.hozur_ghiab.R.drawable.error_dialog_shape;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.MyListData;
import com.example.hozur_ghiab.my_class.Assistance;
import com.example.hozur_ghiab.my_class.Roozh;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.example.hozur_ghiab.my_class.VolleyErrorHelper;
import com.example.hozur_ghiab.remote.data.DataManager;
import com.example.hozur_ghiab.remote.data.DataValues;
import com.example.hozur_ghiab.view.ShowErrorDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VacationRequestViewModel extends ViewModel {
    private SharedPrefManager sharedPrefManager;                           //my sharedPrefManager
    ShowErrorDialog myDialog;                                           //my customDialog to show error messages in a dialog
    DataManager dataManager;
    public MutableLiveData<String> message=new MutableLiveData<>();               //the message we will show in Toast
    public MutableLiveData<Boolean> finishProgress=new MutableLiveData<>();     //says if we should show progressbar or not
    public Boolean vacationSubmited=false;




    public Boolean messageValidate(String myMessage){     //not to show one ToastMessage for several times
        Boolean b;
        if(message.getValue()!=myMessage)
            b=true;
        else b=false;
        return b;
    }

    public String prepareTime(int selectedHour,int selectedMinute){   //to prepare time in format hh:mm
        String preparedTime=selectedHour +":"+selectedMinute;
        if(selectedHour<10 && selectedMinute<10)
            preparedTime ="0"+selectedHour +":"+"0"+selectedMinute;
        else if(selectedHour<10 && selectedMinute>=10)
            preparedTime ="0"+selectedHour +":"+ selectedMinute;
        else if(selectedHour>=10 && selectedMinute<10)
            preparedTime =selectedHour +":"+"0"+selectedMinute;
        return preparedTime;
    }

    public String prepareMessage(String s1,String s2){  //to prepareMessage for Toast
        String result="";
        if(!s1.equals(""))    //if we dont need new line before adding string
            result=s1+"\n"+s2;
        else                 //if we need new line before adding string
            result=s1+s2;
        return result;
    }

    public Boolean checkHourlyValidation(String subject,String description,String date,String timeFrom,String timeTo,
                                         String today,String now) throws ParseException {
        Boolean b=true;
        String myMessage="";

        if(subject.equals("")){       //subject field is empty
            myMessage=prepareMessage(myMessage,"لطفا فیلد موضوع را کامل کنید");
                b=false;
        }

        if(description.equals("")){    //description field is empty
            myMessage=prepareMessage(myMessage,"لطفا فیلد توضیحات را کامل کنید");
            b=false;
        }

        if(date==null){               //date is not selected
            myMessage=prepareMessage(myMessage,"لطفا تاریخ مرخصی را انتخاب کنید");
            b=false;
        }

        if(date!=null ){
            if (Assistance.compareMiladiDates(today,date).equals("after")){   //the selected date is after today
                myMessage=prepareMessage(myMessage,"تاریخ مرخصی انتخاب شده نامعتبر است");
                b=false;
            }
        }

        if(timeFrom==null){        //start time is not selected
            myMessage=prepareMessage(myMessage,"لطفا ساعت شروع را انتخاب کنید");
            b=false;
        }

        if(timeTo==null){         //end time is not selected
            myMessage=prepareMessage(myMessage,"لطفا ساعت پایان را انتخاب کنید");
            b=false;
        }

        if(timeFrom != null && timeTo != null){
            if(Integer.parseInt(timeFrom.substring(0,2)) <9 ){       //start time is before 9:00
                myMessage=prepareMessage(myMessage,"ساعت شروع معتبر نیست");
                b=false;
            }

            if(Integer.parseInt(timeFrom.substring(0,2))>Integer.parseInt(timeTo.substring(0,2))){  //start time is after end time
                myMessage=prepareMessage(myMessage,"ساعت شروع باید پیش از ساعت پایان باشد");
                b=false;
            }

            if(Integer.parseInt(timeFrom.substring(0,2))==Integer.parseInt(timeTo.substring(0,2)) &&
                    Integer.parseInt(timeFrom.substring(3))>Integer.parseInt(timeTo.substring(3))){  //start time is after end time
                myMessage=prepareMessage(myMessage,"ساعت شروع باید پیش از ساعت پایان باشد");
                b=false;
            }

            if(Integer.parseInt(timeFrom.substring(0,2))==Integer.parseInt(timeTo.substring(0,2)) &&
                    Integer.parseInt(timeFrom.substring(3))==Integer.parseInt(timeTo.substring(3))){   //start time is equal to end time
                myMessage=prepareMessage(myMessage,"ساعت شروع باید پیش از ساعت پایان باشد");
                b=false;
            }

            if(Assistance.compareMiladiDates(date,today).equals("equal")){      //so vacation is related to today
                if(Integer.parseInt(timeFrom.substring(0,2))<Integer.parseInt(now.substring(0,2))){  //so the selected time is after now
                    myMessage=prepareMessage(myMessage," زمان شروع وارد شده گذشته است");
                    b=false;
                }

                if(Integer.parseInt(timeFrom.substring(0,2))==Integer.parseInt(now.substring(0,2)) &&
                Integer.parseInt(timeFrom.substring(3))<Integer.parseInt(now.substring(3))){    //so the selected time is after now
                    myMessage=prepareMessage(myMessage," زمان شروع وارد شده گذشته است");
                    b=false;
                }

                if(Integer.parseInt(timeTo.substring(0,2))<Integer.parseInt(now.substring(0,2))){   //so the selected time is after now
                    myMessage=prepareMessage(myMessage," زمان پایان وارد شده گذشته است");
                    b=false;
                }

                if(Integer.parseInt(timeTo.substring(0,2))==Integer.parseInt(now.substring(0,2)) &&
                        Integer.parseInt(timeTo.substring(3))<Integer.parseInt(now.substring(3))){    //so the selected time is after now
                    myMessage=prepareMessage(myMessage," زمان پایان وارد شده گذشته است");
                    b=false;
                }
            }
        }

        if(!myMessage.equals("")){//so we have message to show in Toast
            finishProgress.setValue(true);
            message.setValue(myMessage);
        }
        return b;
    }

    public Boolean checkDailyValidation(String subject,String description,String dateFrom,String dateTo,String today) throws ParseException {
        Boolean b=true;
        String myMessage="";

        if(subject.equals("")){    //title field is empty
            myMessage=prepareMessage(myMessage,"لطفا فیلد موضوع را کامل کنید");
            b=false;
        }

        if(description.equals("")){  //description field is empty
            myMessage=prepareMessage(myMessage,"لطفا فیلد توضیحات را کامل کنید");
            b=false;
        }

        if(dateFrom==null){       //dateFrom is not selected
            myMessage=prepareMessage(myMessage,"لطفا تاریخ شروع مرخصی را انتخاب کنید");
            b=false;
        }

        if(dateTo==null){         //dateTo is not selected
            myMessage=prepareMessage(myMessage,"لطفا تاریخ پایان مرخصی را انتخاب کنید");
            b=false;
        }

        if(dateFrom!=null && dateTo!=null){
            if (Assistance.compareMiladiDates(dateFrom,dateTo).equals("after")){   //dateFrom is after dateTo
                myMessage=prepareMessage(myMessage,"تاریخ پایان باید پیش از تاریخ شروع باشد");
                b=false;
            }
            if (Assistance.compareMiladiDates(today,dateFrom).equals("after")){     //dateFrom is before today
                myMessage=prepareMessage(myMessage,"تاریخ شروع معتبر نیست");
                b=false;
            }
        }

        if(!myMessage.equals("")){    //so we have message to show in Toast
            finishProgress.setValue(true);
        message.setValue(myMessage);
        }

        return b;
    }

    public void hourlyVacationRequest(Context context, Activity activity,String hourlyDate,String timeFrom,String timeTo,String vacationTitle,String vacationDescription) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        Map<String ,String> body=new HashMap<>();   //to send data in body to server
        body.put("type", "hourly");
        body.put("hourly_date", hourlyDate);
        body.put("hourly_from", timeFrom);
        body.put("hourly_to", timeTo);
        body.put("title", vacationTitle);
        body.put("description", vacationDescription);


        dataManager.vacationRequest(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {        //if no error in response
                        vacationSubmited=true;
                        finishProgress.setValue(true);
                        try{
                            if(messageValidate("درخواست شما با موفقیت ثبت شد"))   //message to show in Toast
                                message.setValue("درخواست شما با موفقیت ثبت شد");
                        }catch (NullPointerException e){
                            message.setValue("درخواست شما با موفقیت ثبت شد");
                        }
                    } else {                     //so we have an error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                finishProgress.setValue(true);
                myDialog = new ShowErrorDialog(activity, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));
                    myDialog.show();        //to show dialog

            }
        },headers,body);
    }

    public void dailyVacationRequest(Context context, Activity activity,String dateFrom,String dateTo,String vacationTitle,String vacationDescription) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        Map<String ,String> body=new HashMap<>();     //to send data in body to send to server
        body.put("type", "daily");
        body.put("daily_start", dateFrom);
        body.put("daily_end", dateTo);
        body.put("title", vacationTitle);
        body.put("description", vacationDescription);


        dataManager.vacationRequest(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {        //if no error in response
                        vacationSubmited=true;
                        finishProgress.setValue(true);
                        try{
                            if(messageValidate("درخواست شما با موفقیت ثبت شد "))   //message to show in Toast
                                message.setValue("درخواست شما با موفقیت ثبت شد");
                        }catch (NullPointerException e){
                            message.setValue("درخواست شما با موفقیت ثبت شد");
                        }
                    } else {                     //so we have an error

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                finishProgress.setValue(true);
                myDialog = new ShowErrorDialog(activity, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));

                    myDialog.show();        //to show dialog

            }
        },headers,body);
    }
}