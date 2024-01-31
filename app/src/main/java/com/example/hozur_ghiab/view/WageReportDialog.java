package com.example.hozur_ghiab.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.hozur_ghiab.R;

public class WageReportDialog extends Dialog {

    public Activity c;
    public Dialog d;
    public String valueText, typeText, statusText, createdAtText, descriptionText;
    TextView valueTextView, typeTextView, statusTextView, createdAtTextView, descriptionTextView;
    Button ok;

    public WageReportDialog(Activity a, String value, String type, String status, String createdAt, String description) {     //constructor
        super(a);
        this.c = a;
        valueText = value;
        typeText = type;
        statusText = status;
        createdAtText = createdAt;
        descriptionText = description;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.wage_report_dialog);        //to set the xml layout for this class.
        myInit();    //to initialize variables
        setText();   //to set text to text views
        listener();
    }

    public void myInit() {       //to initialize variables
        valueTextView = findViewById(R.id.value);
        valueTextView.setText(valueText);
        typeTextView = findViewById(R.id.type);
        statusTextView = findViewById(R.id.status);
        createdAtTextView = findViewById(R.id.created_at);
        descriptionTextView = findViewById(R.id.description);
        ok = findViewById(R.id.ok_button);
    }

    public void setText() {
        valueTextView.setText(valueText);
        typeTextView.setText(typeText);
        if (statusText.equals("پرداخت شده")) {
            statusTextView.setTextColor(Color.parseColor("#00AD35"));   //green
        }
        if (statusText.equals("پرداخت نشده")) {
            statusTextView.setTextColor(Color.parseColor("#FF2222"));    //red
        }
        statusTextView.setText(statusText);
        createdAtTextView.setText(createdAtText);
        descriptionTextView.setText(descriptionText);
    }


    public void listener() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //to close the dialog by clicking ok button
            }
        });
    }
}
