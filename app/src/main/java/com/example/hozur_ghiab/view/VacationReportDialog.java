package com.example.hozur_ghiab.view;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.le.ScanSettings;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.hozur_ghiab.R;

public class VacationReportDialog extends Dialog{

        public Activity c;
        public Dialog d;
        public String myTitleText,myCreatedAtText,myStatusText,myDescriptionText,myDetailsText;
        TextView myTitleTextView,myCreatedAtTextView,myStatusTextView,myDescriptionTextView,myDetailsTextView;
        Button ok;

        public  VacationReportDialog(Activity a, String title,String createdAt,String status,String description ,String details) {     //constructor
            super(a);
            this.c = a;
            myTitleText=title;
            myCreatedAtText=createdAt;
            myStatusText=status;
            myDescriptionText=description;
            myDetailsText=details;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.vacation_report_dialog);        //to set the xml layout for this class.
            myInit();    //to initialize variables
            setText();
            listener();
        }

        public void myInit() {       //to initialize variables
            myTitleTextView=findViewById(R.id.title);
            myCreatedAtTextView=findViewById(R.id.created_at);
            myStatusTextView=findViewById(R.id.status);
            myDescriptionTextView=findViewById(R.id.description);
            myDescriptionTextView.setMovementMethod(new ScrollingMovementMethod());  //to make textView scrollable
            myDetailsTextView=findViewById(R.id.details);
            myDetailsTextView.setMovementMethod(new ScrollingMovementMethod());  //to make textView scrollable
            ok=findViewById(R.id.ok_button);
        }

        public void setText() {
            myTitleTextView.setText(myTitleText);
            myCreatedAtTextView.setText(myCreatedAtText);
            myDescriptionTextView.setText(myDescriptionText);
            myDetailsTextView.setText(myDetailsText);

            switch (myStatusText){
                case "requested" :
                    myStatusTextView.setTextColor(Color.parseColor("#707070"));   //gray
                    myStatusTextView.setText("درخواست");
                    break;
                case "applied" :
                    myStatusTextView.setTextColor(Color.parseColor("#00AD35"));   //green
                    myStatusTextView.setText("تایید شده");
                    break;
                case "not_verified":
                    myStatusTextView.setTextColor(Color.parseColor("#FF2222"));   //red
                    myStatusTextView.setText("رد شده");
                    break;
            }
        }


        public void listener(){
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();   //to close the dialog by clicking ok button
                }
            });
        }
    }
