package com.example.hozur_ghiab.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.Item;
import com.example.hozur_ghiab.model.MyListData;
import com.example.hozur_ghiab.model.PieceOfTime;
import com.example.hozur_ghiab.my_class.MyListAdapter;
import com.example.hozur_ghiab.my_class.MyReportsListAdapter;

import java.util.List;

public class CustomReportsDialog extends Dialog {
    public Activity c;
    public Dialog d;
    Item item;
    TextView  descriptionTextView,dateTextView,descriptionText,noWorkTime,noVacationTime
            ,hollidayDescription;
    Button ok;
    RecyclerView vacationRecycle,workTimeRecycle;
    LinearLayout vacationLayOut,workLayOut;


    public CustomReportsDialog(Activity a, Item myItem) {     //constructor
        super(a);
        this.c = a;
        this.item=myItem;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.reports_dialog);        //to set the xml layout for this class.
        myInit();    //to initialize variables
        setItems();
        listener();
    }

    public void myInit() {       //to initialize variables

        descriptionTextView = findViewById(R.id.description);
        ok = findViewById(R.id.ok_button);
        vacationRecycle=findViewById(R.id.vacation_recycle);
        workTimeRecycle=findViewById(R.id.work_time_recycle);
        dateTextView=findViewById(R.id.date);
        descriptionText=findViewById(R.id.description_text);
        noWorkTime=findViewById(R.id.no_work_time);
        noVacationTime=findViewById(R.id.no_vacation_time);
        hollidayDescription=findViewById(R.id.holliday_description);
        vacationLayOut=findViewById(R.id.vacation_layout);
        workLayOut=findViewById(R.id.work_layout);

        dateTextView.setText(item.getDate());
    }

    public void setItems(){
        addToRecycles();
        if(item.getWorkTimes().size()==0) {
            workTimeRecycle.setVisibility(View.GONE);
            noWorkTime.setVisibility(View.VISIBLE);
        }
        if(item.getVacations().size()==0){
            vacationRecycle.setVisibility(View.GONE);
            noVacationTime.setVisibility(View.VISIBLE);
        }
        if(item.getWorkTimes().size()==0 && item.getVacations().size()==0) {
            if (item.getHollidayDescription() != "") {
                vacationRecycle.setVisibility(View.GONE);
                noVacationTime.setVisibility(View.GONE);
                workTimeRecycle.setVisibility(View.GONE);
                noWorkTime.setVisibility(View.GONE);
                vacationLayOut.setVisibility(View.GONE);
                workLayOut.setVisibility(View.GONE);
                hollidayDescription.setVisibility(View.VISIBLE);

                hollidayDescription.setTextColor(Color.parseColor("#FF2222"));
                hollidayDescription.setText(item.getHollidayDescription());

            }
        }
    }
    public void addToRecycles(){
        MyReportsListAdapter adapter=new MyReportsListAdapter(item.getWorkTimes(),c,descriptionTextView,descriptionText);      //to make an object of MyListAdapter type with input : the list of items we made
        workTimeRecycle.setAdapter(adapter);                         //set the adapter to the RecyclerView
        workTimeRecycle.setLayoutManager(new LinearLayoutManager(getContext()));    //to make the components of the list appear linear and  vertical(it is vertical  by default)

        MyReportsListAdapter adapter2=new MyReportsListAdapter(item.getVacations(),c,descriptionTextView,descriptionText);      //to make an object of MyListAdapter type with input : the list of items we made
        vacationRecycle.setAdapter(adapter2);                         //set the adapter to the RecyclerView
        vacationRecycle.setLayoutManager(new LinearLayoutManager(getContext()));    //to make the components of the list appear linear and  vertical(it is vertical  by default)
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
