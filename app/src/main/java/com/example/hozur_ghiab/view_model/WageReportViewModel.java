package com.example.hozur_ghiab.view_model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.hozur_ghiab.R;

import com.example.hozur_ghiab.model.Month;
import com.example.hozur_ghiab.model.MyWageListData;
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
import java.util.Map;

public class WageReportViewModel extends ViewModel {
    public MutableLiveData<ArrayList<MyWageListData>> reportsList=new MutableLiveData<>(); //list to show in recyclerView
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



    public void getReports(Context context, Activity activity, String selectedYear, String selectedMonthValue) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);         //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());


        dataManager.wageReport(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object

                        progressFinished.setValue(true);            //to hide progrssbar

                        JSONArray myItems=obj.getJSONArray("items");
                        if(myItems.length()!=0) {     //so we  have wage reports
                            ArrayList<MyWageListData> myList = new ArrayList<>();
                            for (int j = 0; j < myItems.length(); j++) {                             //to acass elements of myItems JsonArray and add them to myList arrayList
                                MyWageListData wage = new MyWageListData();    //an object of myWageLisData
                                JSONObject myJsonObject = myItems.getJSONObject(j);           //get the jth object of items jsonArray

                                if(myJsonObject.getString("type").equals("USD")){
                                    wage.setType("$");   //to set type for item
                                }
                                else if(myJsonObject.getString("type").equals("IRR")){
                                    wage.setType("IR");    //to set type for item
                                }
                                wage.setValue(NumberFormat.getInstance().format(Integer.parseInt(myJsonObject.getString("value"))).toString()); //to set value for
                                                                                                                                                       //item in format ...,...,
                                wage.setDescription(myJsonObject.getString("description"));   //set description for item
                                wage.setDay(Assistance.convertDateToShamsi(myJsonObject.getString("date")));  //set date for item
                                wage.setCreatedAt(Assistance.convertDateToShamsi(myJsonObject.getString("created_at").substring(0,10)));   //set createdAt for item

                                if (myJsonObject.getString("status").equals("payed")) {   //if status of wage is payed
                                    wage.setStatusBackground(ContextCompat.getDrawable(context, R.drawable.check));  //set status for item(payed type)(for image view)
                                    wage.setStatus("پرداخت شده");   //set status for item(payed type)(for text view)
                                } else if (myJsonObject.getString("status").equals("not_payed")) {  //if status of wage is not payed
                                    wage.setStatus("پرداخت نشده");  //set status for item(not payed type)(for text view)
                                    wage.setStatusBackground(ContextCompat.getDrawable(context, R.drawable.canceal)); //set status for item(not payed type)(for image view)
                                }
                                myList.add(wage);   //add the item to list
                            }
                            reportsList.setValue(myList);  //set list of wageReports to reportsList
                        }
                        else{     //so we dont have any wage report
                            ArrayList<MyWageListData> myList = new ArrayList<>();  // make an empty list
                            reportsList.setValue(myList);
                        }
                        activeMonth=String.valueOf(obj.getInt("active_month"));  //save active month from server
                        activeYear=String.valueOf(obj.getInt("active_year"));     //save active year from server
                        if(!haveMonths.getValue()){            //if we have not saved months yet
                            JSONArray myMonths=obj.getJSONArray("months");
                            for(int i=0;i<myMonths.length();i++){
                                Month myWageMonth=new Month();
                                myWageMonth.setValue(myMonths.getJSONObject(i).getInt("value"));
                                myWageMonth.setTitle(myMonths.getJSONObject(i).getString("title"));
                                months.add(myWageMonth);   //add months to months arrayList
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