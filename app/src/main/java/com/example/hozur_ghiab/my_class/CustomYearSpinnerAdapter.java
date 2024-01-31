package com.example.hozur_ghiab.my_class;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.Month;

import java.util.ArrayList;

public class CustomYearSpinnerAdapter extends ArrayAdapter<String> {


    ArrayList<String> yearsList;
    Boolean isReportsSpinner=false;

    public CustomYearSpinnerAdapter(Context context,
                                    ArrayList<String> yearsList)
    {
        super(context, 0, yearsList);
        this.yearsList=yearsList;
    }

    public void setReportsSpinner( ){
        isReportsSpinner=true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable
            View convertView, @NonNull ViewGroup parent)
    {
        View v= initView(position, convertView, parent);

        TextView yearTextView = v.findViewById(R.id.spinner_text_view);
        LinearLayout myLinearLayout=v.findViewById(R.id.linear_layout);

        //to set style for opening popup items
        myLinearLayout.setBackgroundResource(R.drawable.border_5);
        yearTextView.setTextColor(Color.parseColor("#002793"));
        yearTextView.setGravity(Gravity.CENTER);
        return v;
    }

    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }

        TextView yearTextView = convertView.findViewById(R.id.spinner_text_view);
        String currentItem;


            currentItem = getItem(position);

        if (currentItem != null) {
            yearTextView.setText(currentItem);   //to set the  selected year to spinner text view
            if(isReportsSpinner){
            yearTextView.setGravity(Gravity.RIGHT|Gravity.CENTER);}
        }
        return convertView;
    }
}
