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
import com.example.hozur_ghiab.model.MyVacationListData;
import com.example.hozur_ghiab.model.Month;
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

public class VacationReportViewModel extends ViewModel {

    public MutableLiveData<ArrayList<MyVacationListData>> reportsList=new MutableLiveData<>(); //list to show in recyclerView
    public MutableLiveData<Boolean> progressFinished=new MutableLiveData<>();     //says if we should show progressbar or not

    private SharedPrefManager sharedPrefManager;                           //my sharedPrefManager
    ShowErrorDialog myDialog;                                           //my customDialog to show error messages in a dialog
    DataManager dataManager;

    public ArrayList<Month> months=new ArrayList<>();    //list of months we get from server
    public ArrayList<Integer> years=new ArrayList<>();           //list of years we get from server

    public MutableLiveData<Boolean> haveMonths=new MutableLiveData<>(false);   //shows if we have got months from server or not
    public MutableLiveData<Boolean> haveYears=new MutableLiveData<>(false);    //shows if we have got months from server or not

    public MutableLiveData<String> selectedMonth=new MutableLiveData<>("");    //user Selected Month
    public MutableLiveData<String> selectedYear=new MutableLiveData<>("");     //user selected year

    public String activeMonth="";   //this month
    public String activeYear="";    //this year

    public String errorMessage="";


    public void getReports(Context context, Activity activity,String selectedYear,String selectedMonthValue) {
        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);         //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        dataManager.vacationReport(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {     //if no error in response
                        progressFinished.setValue(true);            //to hide progrssbar
                        JSONArray myItems=obj.getJSONArray("items");
                        if(myItems.length()!=0) {     //so we  have  vacation reports
                            ArrayList<MyVacationListData> myList = new ArrayList<>();
                            for (int j = 0; j < myItems.length(); j++) {                             //to acass elements of myItems JsonArray and add them to myList arrayList
                                MyVacationListData vacationTime = new MyVacationListData();    //an object of myVavcationLisData
                                JSONObject myJsonObject = myItems.getJSONObject(j);           //get the jth object of items jsonArray

                                vacationTime.setTitle(myJsonObject.getString("title"));    //set title for item
                                vacationTime.setDescription(myJsonObject.getString("description"));   //set description for item
                                vacationTime.setStatus(myJsonObject.getString("status"));         //set status for item

                                vacationTime.setCreatedAt(Assistance.convertDateToShamsi(myJsonObject.getString("created_at").substring(0,10)));   //set createdAt for item

                                if (myJsonObject.getString("type").equals("hourly")) {   //if type of vacation is hourly
                                    vacationTime.setTypeBackground(ContextCompat.getDrawable(context, R.drawable.hourly_vacation));  //set type for item(hourlyType)
                                    vacationTime.setDay(Assistance.defineMonthName(Assistance.convertDateToShamsi(myJsonObject.getJSONObject("data").
                                            getString("date")).substring(5, 7))+" "+
                                            Assistance.removeZero(Assistance.convertDateToShamsi(myJsonObject.getJSONObject("data").getString("date")).
                                                    substring(8))  //set day for item
                                    );

                                    vacationTime.setDetails(     //set details for item
                                            "مرخصی در روز : " + Assistance.convertDateToShamsi(myJsonObject.getJSONObject("data").toString().substring(9, 19)) + "\n" +
                                                    "از ساعت : " + myJsonObject.getJSONObject("data").toString().substring(29, 34)  +
                                                    "  تا  " + myJsonObject.getJSONObject("data").toString().substring(45, 50)
                                    );
                                } else if (myJsonObject.getString("type").equals("daily")) {   //if vacation type is daily

                                    vacationTime.setTypeBackground(ContextCompat.getDrawable(context, R.drawable.daily_vacation)); //set type for item(dailyType)

                                    vacationTime.setDay(Assistance.defineMonthName(Assistance.convertDateToShamsi(myJsonObject.getJSONArray("data").
                                            getString(0)).substring(5, 7))+" "+Assistance.removeZero(Assistance.convertDateToShamsi(myJsonObject.getJSONArray("data").
                                            getString(0)).substring(8))
                                    );   //set day for item
                                    if(myJsonObject.getJSONArray("data").length()==1){   //so the vacation is for one day
                                        vacationTime.setDetails(
                                                "مرخصی در روز : " + Assistance.convertDateToShamsi(myJsonObject.getJSONArray("data").getString(0)));
                                    }
                                    else {         //so the vacation is for more than one day
                                    vacationTime.setDetails(
                                            "مرخصی از روز : " + Assistance.convertDateToShamsi(myJsonObject.getJSONArray("data").getString(0)) + "\n" +
                                                    "تا روز : " + Assistance.convertDateToShamsi(myJsonObject.getJSONArray("data").getString(myJsonObject.getJSONArray("data").length()-1))
                                    );
                                    }
                                }
                                myList.add(vacationTime);   //add the item to list
                            }
                            reportsList.setValue(myList);  //set list of vacationReports to reportsList
                        }
                        else{     //so we dont have any vacation report
                            ArrayList<MyVacationListData> myList = new ArrayList<>();  // make an empty list
                            reportsList.setValue(myList);
                        }
                        activeMonth=String.valueOf(obj.getInt("active_month"));  //save active month from server
                        activeYear=String.valueOf(obj.getInt("active_year"));     //save active year from server
                        if(!haveMonths.getValue()){            //if we have not saved months yet
                            JSONArray myMonths=obj.getJSONArray("months");
                            for(int i=0;i<myMonths.length();i++){
                                Month myVacationMonth=new Month();
                                myVacationMonth.setValue(myMonths.getJSONObject(i).getInt("value"));
                                myVacationMonth.setTitle(myMonths.getJSONObject(i).getString("title"));
                                months.add(myVacationMonth);   //add months to months arrayList
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
                if(!errorMessage.equals(VolleyErrorHelper.getErrorType(volleyError,context))) {
                    myDialog.show();        //to show dialog
                    errorMessage=VolleyErrorHelper.getErrorType(volleyError,context);
                }
            }
        },headers, selectedYear, selectedMonthValue);
    }
}