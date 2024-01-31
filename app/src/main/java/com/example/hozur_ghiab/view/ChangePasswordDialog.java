package com.example.hozur_ghiab.view;

import static com.example.hozur_ghiab.R.drawable.error_dialog_shape;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.example.hozur_ghiab.my_class.VolleyErrorHelper;
import com.example.hozur_ghiab.remote.data.DataManager;
import com.example.hozur_ghiab.remote.data.DataValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class ChangePasswordDialog extends Dialog {
    public Activity c;
    public Dialog d;
    Context context;
    ImageView exitButton;
    Button okButton;
    EditText password,confirmPassword;
    private SharedPrefManager sharedPrefManager;                           //my sharedPrefManager
    ShowErrorDialog myDialog;                                           //my customDialog to show error messages in a dialog
    DataManager dataManager;

    public ChangePasswordDialog(Activity a,Context c) {     //constructor
        super(a);
        this.c = a;
        this.context=c;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_password_dialog);        //to set the xml layout for this class.
        myInit();    //to initialize variables
        listener();
    }

    public void myInit(){
        okButton=findViewById(R.id.ok_button);
        exitButton=findViewById(R.id.exit_button);
        password=findViewById(R.id.password);
        confirmPassword=findViewById(R.id.confirm_password);
    }


    public Boolean checkVlidation(){
        Boolean b=true;
        if(password.getText().toString().equals("") && confirmPassword.getText().toString().equals("") || password.getText().toString().equals("") &&
                !confirmPassword.getText().toString().equals("") ){
            showToast("لطفا رمز جدید را وارد کنید");
            b=false;
        }
        else if (!password.getText().toString().equals("") && confirmPassword.getText().toString().equals("")){
            showToast("لطفا تکرار رمز جدید را وارد کنید");
            b=false;
        }

        else if (!password.getText().toString().equals("") && !confirmPassword.getText().toString().equals("")) {
            if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                showToast("رمز وارد شده و تکرار آن  باهم برابر نیستند");
                b = false;
            }
            else if (password.getText().toString().length()<6 ){
                showToast("رمز عبور نباید کم تر از 6 کارکتر باشد");
                b=false;
            }
        }
        return b;
    }

    public void listener(){
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkVlidation()){
                    setPassword();
                }
            }
        });
    }

    public void showToast(String message){
        Toast.makeText(context,message,
                Toast.LENGTH_SHORT).show();
    }

    public void setPassword() {

        dataManager=new DataManager();
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager

        Map<String, String> headers=new HashMap<String, String>();            //my headers HashMap to send my token to server
        headers.put("Authorization", "Bearer "+sharedPrefManager.getToken());

        Map<String ,String> body=new HashMap<>();   //to send data in body to server
        body.put("password", password.getText().toString());


        dataManager.changePassword(context, new DataValues() {
            @Override
            public void setJsonDataResponse(String response) {
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);                    //converting response to json object
                    if ((obj.getInt("status") == 200)) {        //if no error in response
                        showToast("رمز شما با موفقیت تغییر کرد");
                            dismiss();
                    } else {                     //so we have an error
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void setVolleyError(VolleyError volleyError) throws UnsupportedEncodingException, JSONException {
                myDialog = new ShowErrorDialog(c, VolleyErrorHelper.getErrorType(volleyError,context));     //an object of ShowErrorDialog class to show error
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(error_dialog_shape));      //set style for errorDialog
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));
                myDialog.show();        //to show dialog
            }
        },body,headers);
    }
}
