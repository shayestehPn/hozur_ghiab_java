package com.example.hozur_ghiab.my_class;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.hozur_ghiab.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VolleyErrorHelper {


    public static String getErrorType(Object error, Context context) throws UnsupportedEncodingException, JSONException {
        if (error instanceof TimeoutError) {
            return context.getResources().getString(R.string.generic_server_timeout);
        }
        else if (error instanceof ServerError) {
            VolleyError myError = (VolleyError) error;
            String responseData = new String(myError.networkResponse.data,"UTF-8");
            JSONObject responseObject =new  JSONObject(responseData);
            JSONArray myJsonArray=responseObject.getJSONArray("errors");
            String message="";
            for(int i=0;i<myJsonArray.length();i++){   //to show all error messageswe got from server
                if(i!=myJsonArray.length()-1)
                message=message+myJsonArray.getString(i)+"\n";
                else
                    message=message+myJsonArray.getString(i);
            }
            return message;

        } else if (error instanceof AuthFailureError) {
            return context.getResources().getString(R.string.auth_failed);
        } else if (error instanceof NetworkError) {
            return context.getResources().getString(R.string.no_internet);
        } else if (error instanceof NoConnectionError) {
            return context.getResources().getString(R.string.no_network_connection);
        } else if (error instanceof ParseError) {
            return context.getResources().getString(R.string.parsing_failed);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }
}
