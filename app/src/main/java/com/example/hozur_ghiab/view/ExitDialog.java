package com.example.hozur_ghiab.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.Item;
import com.example.hozur_ghiab.my_class.SharedPrefManager;

public class ExitDialog extends Dialog {
    public Activity activity;
    public Dialog d;
    Button yesButton,noButton;
    private SharedPrefManager sharedPrefManager;                           //my sharedPrefManager
    Context context;
    public NavController navController;


    public ExitDialog(Activity a,Context c) {     //constructor
        super(a);
        this.activity = a;
        context=c;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.exit_dialog);        //to set the xml layout for this class.
        myInit();    //to initialize variables
        listener();
    }

    public void myInit(){
        yesButton=findViewById(R.id.yes_button);
        noButton=findViewById(R.id.no_button);
        sharedPrefManager=new SharedPrefManager(context);     //my sharedPrefManager
        navController = Navigation.findNavController(activity, R.id.nav_host_fragment);
    }

    public void listener(){
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefManager.setToken("");
                dismiss();
                navController.navigate(R.id.splash);   //to go to dashboard fragment
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
