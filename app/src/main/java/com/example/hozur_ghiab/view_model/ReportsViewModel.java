package com.example.hozur_ghiab.view_model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.Item;
import com.example.hozur_ghiab.model.Month;
import com.example.hozur_ghiab.model.MyWageListData;
import com.example.hozur_ghiab.model.PieceOfTime;
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsViewModel extends ViewModel {
    public MutableLiveData<Boolean> progressFinished=new MutableLiveData<>();     //says if we should show progressbar or not

    private SharedPrefManager sharedPrefManager;                           //my sharedPrefManager
    ShowErrorDialog myDialog;                                           //my customDialog to show error messages in a dialog
    DataManager dataManager;

    public ArrayList<Month> months=new ArrayList<>();    //list of months we get from server
    public ArrayList<Integer> years=new ArrayList<>();           //list of years we get from server

    public MutableLiveData<Boolean> haveMonths=new MutableLiveData<>(false);   //shows if we have got months from server or not
    public MutableLiveData<Boolean> haveYears=new MutableLiveData<>(false);    //shows if we have got months from server or not
    public MutableLiveData<Boolean> haveData=new MutableLiveData<>(false);    //shows if we have got months from server or not


    public MutableLiveData<String> selectedMonth=new MutableLiveData<>("");    //user Selected Month
    public MutableLiveData<String> selectedYear=new MutableLiveData<>("");     //user selected year

    public String activeMonth="";   //this month
    public String activeYear="";    //this year

    public String extraTime,totalTime,delayTime,vacationTime,usdProfit,irrProfit,dayOfTheWeek;


    public ArrayList<Item> itemsList;           //a list to add objects of type Item

    public Boolean haveActiveMonthAndYear=false;

    public String errorMessage="";



    public String minus(String  firstNumber,String secondNumber){  //to get  firstNumber - secondNumber
        String result="";
        int first =Integer.parseInt(firstNumber);
        int second=Integer.parseInt(secondNumber);
        if(first-second<=0)
            result="";
        else
            result=String.valueOf(first-second);
        return result;
    }


    public void getReports(Context context, Activity activity, String selectedYear, String selectedMonthValue) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);         //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+ sharedPrefManager.getToken());


        dataManager.report(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                System.out.println("=====9"+response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    System.out.println("999"+response);
                    progressFinished.setValue(true);            //to hide progrssbar

                    JSONObject myMainreports=obj.getJSONObject("cards");
                    extraTime=myMainreports.getString("extra_time");
                    totalTime=myMainreports.getString("total_time");
                    delayTime=myMainreports.getString("delay");
                    vacationTime=myMainreports.getString("vacation_time");
                    usdProfit=myMainreports.getString("USD_profit");
                    irrProfit=myMainreports.getString("IRR_profit");

                    if(!haveActiveMonthAndYear) {
                        activeMonth=String.valueOf(obj.getInt("active_month"));  //save active month from server
                        activeYear=String.valueOf(obj.getInt("active_year"));     //save active year from server
                        haveActiveMonthAndYear=true;
                    }


                    dayOfTheWeek=String.valueOf(obj.getString("dayOfTheWeek"));   //save day of the first day of the month

                    itemsList = new ArrayList<>();

                    JSONArray items=obj.getJSONArray("items");                    //get JSONArray of response with key : items


                    for(int i = 0; i < items.length(); i++){                 //to acass elements of items JsonArray and add them to itemsList arrayList
                        Item item=new Item();                                //an object of type Item

                        item.setDate(items.getJSONObject(i).getString("date"));                                //set date for item

                        JSONArray workTimes=items.getJSONObject(i).getJSONArray("work_times");    //get JSONArray of every item in the items with key : work_times
                        for(int j = 0; j < workTimes.length(); j++){                             //to acass elements of workTimes JsonArray and add them to item workTimes arrayList
                            PieceOfTime workTime=new PieceOfTime();

                            workTime.setStart(workTimes.getJSONObject(j).getString("start"));

                            if(workTimes.getJSONObject(j).getString("end").equals("null"))
                                workTime.setEnd("ثبت نشده");
                            else
                            workTime.setEnd(workTimes.getJSONObject(j).getString("end"));

                            String description=workTimes.getJSONObject(j).getString("description");

                            description=description.replace("<p>","");
                            description=description.replace("</p>","");    //to remove html tags from description
                            if(description.equals("null")){
                                description="توضیحات عملکرد ثبت نشده";
                            }
                            workTime.setDescription(description);

                            item.addToWorkTimes(workTime);                                          //add workTime to the list of workTimes in item

                        }


                        JSONArray vacations = items.getJSONObject(i) .getJSONArray("vacations");
                        for(int j = 0; j < vacations.length(); j++){
                            PieceOfTime vacation=new PieceOfTime();
                            if(vacations.getJSONObject(j).getString("type").equals("hourly")){
                            vacation.setStart(vacations.getJSONObject(j).getJSONObject("data").getString("from"));
                            vacation.setEnd(vacations.getJSONObject(j).getJSONObject("data").getString("to"));
                            }
                            else if(vacations.getJSONObject(j).getString("type").equals("daily")){
                                vacation.setStart(vacations.getJSONObject(j).getJSONArray("data").getString(0));
                                vacation.setEnd(vacations.getJSONObject(j).getJSONArray("data").getString(vacations.getJSONObject(j).
                                        getJSONArray("data").length()-1));
                            }
                            vacation.setDescription(vacations.getJSONObject(j).getString("description"));

                            item.addToVacations(vacation);

                        }
                        if(!items.getJSONObject(i).getString("holiday").equals("null")) {
                        JSONObject hollidayObject = items.getJSONObject(i) .getJSONObject("holiday");
                            item.setHollidayDescription(hollidayObject.getString("description"));
                        }
                            itemsList.add(item);

                    }

                    haveData.setValue(true);

                    if(!haveMonths.getValue()){            //if we have not saved months yet
                        JSONArray myMonths=obj.getJSONArray("months");
                        for(int i=0;i<myMonths.length();i++){
                            Month myMonth=new Month();
                            myMonth.setValue(myMonths.getJSONObject(i).getInt("value"));
                            myMonth.setTitle(myMonths.getJSONObject(i).getString("title"));
                            months.add(myMonth);   //add months to months arrayList
                        }
                        haveMonths.setValue(true);   //now we have months and dont need to save them next time
                    }

                    if(!haveYears.getValue()){    //if we have not saved years yet
                        JSONArray myYears=obj.getJSONArray("years");
                        for(int i=0;i<myYears.length();i++){
                            years.add(myYears.getInt(i));   //add years to years arrayList
                        }
                        haveYears.setValue(true);     //now we have years and dont need to save them next time
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
                if(!errorMessage.equals(VolleyErrorHelper.getErrorType(volleyError,context))) {
                    myDialog.show();        //to show dialog
                    errorMessage=VolleyErrorHelper.getErrorType(volleyError,context);
                }
            }
        },headers, selectedYear, selectedMonthValue);
    }

}