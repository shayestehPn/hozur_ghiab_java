package com.example.hozur_ghiab.remote.data;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;

public interface DataValues {

    public  void setJsonDataResponse(String response);
    public  void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException;


}
