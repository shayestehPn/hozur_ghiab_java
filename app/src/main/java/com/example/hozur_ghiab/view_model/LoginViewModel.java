package com.example.hozur_ghiab.view_model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.hozur_ghiab.my_class.Assistance;
import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.example.hozur_ghiab.remote.data.DataManager;
import com.example.hozur_ghiab.remote.data.DataValues;
import com.example.hozur_ghiab.view.ShowErrorDialog;
import com.example.hozur_ghiab.my_class.VolleyErrorHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> message=new MutableLiveData<>();               //the message we will show in Toast
    public MutableLiveData<Boolean> progressFinished=new MutableLiveData<>();     //says if we should show progressbar or not
    private SharedPrefManager sharedPrefManager;
    ShowErrorDialog myDialog;                                           //my ShowErrorDialog to show error messages in a dialog
    DataManager dataManager;





    public Boolean messageValidate(String myMessage){     //not to show one ToastMessage for several times
        Boolean b;
        if(message.getValue()!=myMessage)
            b=true;
        else b=false;
        return b;
    }

    //login method
    public void loginUser(String myMobileNumber, String myPassword, Context context, Activity activity) {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager

        if (TextUtils.isEmpty(Assistance.convert(myMobileNumber)) || !Assistance.isValid(Assistance.convert(myMobileNumber)) ) {   //to show error if mobilePhone is invalid or empty
            try{
                if(messageValidate("لطفا شماره موبایل صحیح خود را وارد کنید"))
                    message.setValue("لطفا شماره موبایل صحیح خود را وارد کنید");
            }catch (NullPointerException e){
                message.setValue("لطفا شماره موبایل صحیح خود را وارد کنید");
            }
            progressFinished.setValue(true);                                    //to finish showing progressDialog to show Toast
            return;
        }

        if (TextUtils.isEmpty(myPassword)) {                               //to show error if password is  empty
            try{
                if(messageValidate("لطفا پسوورد خود را وارد کنید"))
                    message.setValue("لطفا پسوورد خود را وارد کنید");
            }catch (NullPointerException e){
                message.setValue("لطفا پسوورد خود را وارد کنید");
            }
            progressFinished.setValue(true);                               //to finish showing progressDialog to show Toast
            return;
        }

        Map<String ,String> body=new HashMap<>();                        //body to send mobileNumber and password to server
        body.put("mobile", Assistance.convert(myMobileNumber));
        body.put("password", myPassword);

        dataManager.login(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                progressFinished.setValue(true);                        //to hide progressbar if we get the response from server
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {        //if no error in response
                        try{
                            if(messageValidate("شما با موفقیت وارد شدید."))
                                message.setValue("شما با موفقیت وارد شدید.");
                        }catch (NullPointerException e){
                            message.setValue("شما با موفقیت وارد شدید.");
                        }
                        String token=obj.getString("token");    //to get the token from the response with key : token
                        sharedPrefManager.setToken(token);             //to save the token in my sharedPrefrence file
                    } else {                     //so we have an error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                progressFinished.setValue(true);
                myDialog = new ShowErrorDialog(activity, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));

                    myDialog.show();        //to show dialog

            }
        },body);
    }
}