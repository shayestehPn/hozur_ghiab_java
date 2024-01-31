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

public class CustomMonthSpinnerAdapter extends ArrayAdapter<Month> {


    ArrayList<Month> monthsList;
    Boolean isReportsSpinner=false;
    public CustomMonthSpinnerAdapter(Context context,
                                     ArrayList<Month> monthsList)
    {
        super(context, 0, monthsList);
        this.monthsList=monthsList;
    }
    public void setReportsSpinner(){
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

        TextView monthTextView = v.findViewById(R.id.spinner_text_view);
        LinearLayout myLinearLayout=v.findViewById(R.id.linear_layout);

        //to set style for opening popup items
        myLinearLayout.setBackgroundResource(R.drawable.border_5);            //border for items of opening popup

        monthTextView.setTextColor(Color.parseColor("#002793"));   //blue textcolor for items of opening popup
        monthTextView.setGravity(Gravity.CENTER);
        return v;
    }



    private View initView(int position, View convertView,
                          ViewGroup parent)
    {
        // It is used to set our custom view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_spinner_item, parent, false);
        }
        TextView monthTextView = convertView.findViewById(R.id.spinner_text_view);
        Month currentItem=null;

         currentItem = getItem(position);

        if (currentItem != null) {
            monthTextView.setText(currentItem.getTitle());   //to set the title of selected month to spinner text view
            if(isReportsSpinner){
                monthTextView.setGravity(Gravity.RIGHT|Gravity.CENTER);
            }
        }
        return convertView;
    }
}
