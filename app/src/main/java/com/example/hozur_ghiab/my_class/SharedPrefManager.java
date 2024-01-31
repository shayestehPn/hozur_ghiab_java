package com.example.hozur_ghiab.my_class;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "volleylogin";
    private static final String KEY_Mobile = "mobile";               //key for mobile number based on api
    private static final String KEY_PASSWORD = "password";           //key for password based on api
    private static final String KEY_TOKEN = "token";                 //key for token based on api
    private static final String KEY_NAME = "name";                 //key for name based on api

    private SharedPreferences sharedPreferences;
    private static Context ctx;

    public SharedPrefManager(Context context) {        //constructor
        this.sharedPreferences = context.getSharedPreferences("SHARED_PREF_NAME",
                Context.MODE_PRIVATE);
        this.ctx = context;
    }

    public String getMobile() {  //to get the mobile number of the user loged in from the sharedPrefrence file and s1 is its default value if the user is not loged in and it doesnt find any value with key : mobile in users sharedPrefrence file

        return sharedPreferences.getString(KEY_Mobile, "");
    }

    public String getPassword() {
        return sharedPreferences.getString(KEY_PASSWORD, "");
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, "");
    }

    public String getName(){
        return sharedPreferences.getString(KEY_NAME,"");
    }


    public void setMobile(String mobile) {                          //to save the mobile number of the user in the sharedPrefrence file
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.KEY_Mobile, mobile);
        edit.apply();
    }

    public void setPassword(String password) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(this.KEY_PASSWORD, password);
        edit.apply();
    }

    public void setToken(String token){                                  //to save the token that server gave the user in the response in the sharedPrefrence file
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_TOKEN,token );
        editor.apply();
    }

    public void setName(String name){                                  //to save the name that server gave the user in the response in the sharedPrefrence file
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_NAME,name);
        editor.apply();
    }
}
