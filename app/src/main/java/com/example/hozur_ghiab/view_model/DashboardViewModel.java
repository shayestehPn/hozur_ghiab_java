package com.example.hozur_ghiab.view_model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.MyListData;
import com.example.hozur_ghiab.my_class.Assistance;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.example.hozur_ghiab.my_class.VolleyErrorHelper;
import com.example.hozur_ghiab.remote.data.DataManager;
import com.example.hozur_ghiab.remote.data.DataValues;
import com.example.hozur_ghiab.view.ShowErrorDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardViewModel extends ViewModel {
    public MutableLiveData<String> message=new MutableLiveData<>();               //the message we will show in Toast
    public MutableLiveData<Boolean> hasEntered=new MutableLiveData<>();     //says if user had entered or not
    public MutableLiveData<Boolean> progressFinished=new MutableLiveData<>();     //says if we should show progressbar or not
    public MutableLiveData<ArrayList<MyListData>> reportsList=new MutableLiveData<>(); //list to show in recyclerView
    Boolean listHasDefaultValue=false;                                                 //it is true if list has one row with "..." values
    private SharedPrefManager sharedPrefManager;                           //my sharedPrefManager
    ShowErrorDialog myDialog;                                           //my customDialog to show error messages in a dialog
    DataManager dataManager;
    public String date,enterTime,exitTime,description;
    public MutableLiveData<String> name=new MutableLiveData<>("");               //name to show in header of navigation drawer

    public String errorMessage="";

    public Boolean messageValidate(String myMessage){     //not to show one ToastMessage for several times
        Boolean b;
        if(message.getValue()!=myMessage)
            b=true;
        else b=false;
        return b;
    }


    public void getDashboard( Context context, Activity activity) {    //to get dashboard and state of user (entered,exited,etc)
        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);         //my sharedPrefManager

         Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
            headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        dataManager.dashboard(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {     //if no error in response
                        progressFinished.setValue(true);
                        JSONObject items=obj.getJSONObject("items");
                        JSONObject user=items.getJSONObject("user");
                        String userName=user.getString("name");    //to get the name from the response with key : name
                        sharedPrefManager.setName(userName);             //to save the name in my sharedPrefrence file
                        name.setValue(userName);
                        if(!items.getString("users_latest_time").equals("null")) {    //user may have entered or not we should check it
                            JSONObject latestTime = items.getJSONObject("users_latest_time");
                            String myEnterTime = latestTime.getString("start");
                            String myExitTime = latestTime.getString("end");

                            if ((!myEnterTime.equals("null")) && (myExitTime.equals("null"))) { //so user has entered
                                enterTime = myEnterTime;              //to save user enter time
                                date = Assistance.convertDateToShamsi(latestTime.getString("date"));   //to save date of today
                                hasEntered.setValue(true);
                            } else {                                  //so user has not entered
                                hasEntered.setValue(false);
                            }
                        }
                        else{                                       //so user has not entered
                            hasEntered.setValue(false);
                        }

                        JSONArray myJsonArray=items.getJSONArray("users_today_times");
                        ArrayList<MyListData> myList=new ArrayList<>();
                        for(int j =  myJsonArray.length()-1; j >=0; j--){                             //to acass elements of myJsonAraray JsonArray and add them to myList arrayList
                            MyListData workTime=new MyListData();
                            if(!myJsonArray.getJSONObject(j).getString("start").equals("null")){   //so user has enter time
                            workTime.setEnterTime(myJsonArray.getJSONObject(j).getString("start"));}
                            else{                       //so user has not enter time,we should set default value
                                workTime.setEnterTime("...");}
                            if(!myJsonArray.getJSONObject(j).getString("end").equals("null"))
                            {workTime.setExitTime(myJsonArray.getJSONObject(j).getString("end"));}
                            else
                            {workTime.setExitTime("...");}

                            if(!myJsonArray.getJSONObject(j).getString("description").equals("null"))
                            {

                                String description=myJsonArray.getJSONObject(j).getString("description").replace("<p>","");
                                 description=description.replace("</p>",""); //to remove html tags from description
                                workTime.setDescription(description);
                            }
                            else
                            {workTime.setDescription("...");}
                            myList.add(workTime);                              //add workTime to the list of workTimes
                                                  //set list of workTimes to reports list
                        }
                        if(myJsonArray.length()==0){                          //so user has never entered today
                            MyListData m=new MyListData();                    //it has values "..." by default
                            myList.add(m);
                            listHasDefaultValue=true;
                        }
                        reportsList.setValue(myList);
                    } else {                     //so we have an error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                myDialog = new ShowErrorDialog(activity, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));
                progressFinished.setValue(true);
                myDialog.show();        //to show dialog
            }
        },headers);
    }

    public void enterUser( Context context, Activity activity) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        dataManager.enter(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if(listHasDefaultValue){                           //if list has one row with  values : "..."  ,make it empty
                        ArrayList<MyListData> list=new ArrayList<>();
                        reportsList.setValue(list);
                        listHasDefaultValue=false;
                    }
                    if ((obj.getInt("status") == 200)) {        //if no error in response
                        progressFinished.setValue(true);
                        JSONObject item=obj.getJSONObject("item");
                        enterTime=item.getString("start");              //to save user enter time
                        date=Assistance.convertDateToShamsi(item.getString("date"));             //to save date of today
                        MyListData workTime=new MyListData();
                        workTime.setEnterTime(enterTime);
                        ArrayList<MyListData> list=new ArrayList<>();
                        for(int i=0;i<reportsList.getValue().size();i++){
                            list.add(reportsList.getValue().get(i));
                        }
                        list.add(workTime);             //to add new workTime to the list of workTimes
                        reportsList.setValue(list);
                        hasEntered.setValue(true);
                        try{
                            if(messageValidate("ورود شما با موفقیت ثبت شد"))     //to set message to show in Toast
                                message.setValue("ورود شما با موفقیت ثبت شد");
                        }catch (NullPointerException e){
                            message.setValue("ورود شما با موفقیت ثبت شد");
                        }
                    } else {                     //so we have an error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                myDialog = new ShowErrorDialog(activity, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));
                progressFinished.setValue(true);
                    myDialog.show();        //to show dialog

            }
        },headers);
    }

    public void exitUser( Context context, Activity activity,String myDescription) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        Map<String ,String> body=new HashMap<>();              //body to send description to server
        body.put("description", myDescription);

        dataManager.exit(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {        //if no error in response
                        progressFinished.setValue(true);
                        JSONObject item=obj.getJSONObject("item");
                         exitTime=item.getString("end");                  //to save user exit time
                        enterTime=item.getString("start");              //to save user enter time
                        String description=item.getString("description");        //to save user descriptione
                        description=description.replace("<p>","");
                        description=description.replace("</p>","");    //to remove html tags from description

                        reportsList.getValue().get(reportsList.getValue().size()-1).setExitTime(exitTime);    //to set exit time for last item of list
                        reportsList.getValue().get(reportsList.getValue().size()-1).setDescription(description); //to set description for last item of list

                        ArrayList<MyListData> list=new ArrayList<>();     //list of items of reportsList
                        for(int i=0;i<reportsList.getValue().size();i++){
                            list.add(reportsList.getValue().get(i));
                        }
                        reportsList.setValue(list);
                        try{
                            if(messageValidate("خروج شما با موفقیت ثبت شد "))   //message to show in Toast
                                message.setValue("خروج شما با موفقیت ثبت شد");
                        }catch (NullPointerException e){
                            message.setValue("خروج شما با موفقیت ثبت شد");
                        }
                        hasEntered.setValue(false);
                    } else {                     //so we have an error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                myDialog = new ShowErrorDialog(activity, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));
                progressFinished.setValue(true);
                myDialog.show();        //to show dialog
                errorMessage=VolleyErrorHelper.getErrorType(volleyError,context);
            }
        },headers,body);
    }
    public Boolean haveDescription(String description){
        Boolean b=true;
        if (description.equals("")){
                b=false;
                message.setValue("فیلد توضیحات الزامی میباشد.");
        }
        return b;
    }
}