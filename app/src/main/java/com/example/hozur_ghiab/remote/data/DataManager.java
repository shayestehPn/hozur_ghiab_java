package com.example.hozur_ghiab.remote.data;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.hozur_ghiab.remote.ApiCall;
import com.example.hozur_ghiab.remote.VolleySingleton;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class DataManager {


    public void login(Context context, final DataValues dataValues, Map<String ,String> body){

        StringRequest request=new StringRequest(Request.Method.POST, ApiCall.BaseURL + "/login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void dashboard(Context context, final DataValues dataValues, Map<String ,String> headers){

        StringRequest request=new StringRequest(Request.Method.GET, ApiCall.BaseURL + "/dashboard", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }


    public void enter(Context context, final DataValues dataValues, Map<String ,String> headers){

        StringRequest request=new StringRequest(Request.Method.POST, ApiCall.BaseURL + "/set/entrance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        })
        {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void exit(Context context, final DataValues dataValues, Map<String ,String> headers,Map<String ,String> body){

        StringRequest request=new StringRequest(Request.Method.POST, ApiCall.BaseURL + "/set/exit", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void vacationRequest(Context context, final DataValues dataValues, Map<String ,String> headers,Map<String ,String> body){

        StringRequest request=new StringRequest(Request.Method.POST, ApiCall.BaseURL + "/vacation-request", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void vacationReport(Context context, final DataValues dataValues, Map<String ,String> headers,String selectedYear,String selectedMonthValue
    ){
        String filter="";  // to filter month and year
        filter="?"+"year="+selectedYear+"&month="+selectedMonthValue;
        if(selectedMonthValue=="" && selectedYear==""){   //so we dont need to filter
            filter="";
        }
        StringRequest request=new StringRequest(Request.Method.GET, ApiCall.BaseURL + "/vacation-report"+filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
    public void wageReport(Context context, final DataValues dataValues, Map<String ,String> headers,String selectedYear,String selectedMonthValue
    ){
        String filter="";  // to filter month and year
        filter="?"+"year="+selectedYear+"&month="+selectedMonthValue;
        if(selectedMonthValue=="" && selectedYear==""){   //so we dont need to filter
            filter="";
        }
        StringRequest request=new StringRequest(Request.Method.GET, ApiCall.BaseURL + "/profits"+filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        })
        {

            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void report(Context context, final DataValues dataValues, Map<String ,String> headers,String selectedYear,String selectedMonthValue
    ){
        String filter="";  // to filter month and year
        filter="?"+"year="+selectedYear+"&month="+selectedMonthValue;
        if(selectedMonthValue=="" && selectedYear==""){   //so we dont need to filter
            filter="";
        }
        StringRequest request=new StringRequest(Request.Method.GET, ApiCall.BaseURL + "/reports"+filter, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        })
        {
            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }

    public void changePassword(Context context, final DataValues dataValues, Map<String ,String> body,Map<String ,String> headers){

        StringRequest request=new StringRequest(Request.Method.POST, ApiCall.BaseURL + "/change-password", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                dataValues.setJsonDataResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    dataValues.setVolleyError(error);
                } catch (UnsupportedEncodingException | JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return body;
            }

            @Nullable
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(request);
    }
}
