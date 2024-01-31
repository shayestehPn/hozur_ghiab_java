package com.example.hozur_ghiab.my_class;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.MyVacationListData;
import com.example.hozur_ghiab.model.PieceOfTime;
import com.example.hozur_ghiab.view.VacationReportDialog;

import java.util.ArrayList;
import java.util.List;

public class MyReportsListAdapter extends RecyclerView.Adapter<MyReportsListAdapter.ViewHolder> {

    private List<PieceOfTime> listdata=new ArrayList<>();      //the list of items of type MyVacationListData
    TextView descriptionTextView,descriptionText;
    Activity activity;

    public MyReportsListAdapter(List<PieceOfTime> listdata, Activity a,TextView myDescriptionTextView,TextView myDescriptionText) {  //constructor
        this.listdata = listdata;
        activity=a;
        this.descriptionTextView=myDescriptionTextView;
        this.descriptionText=myDescriptionText;
    }

    @Override
    public MyReportsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {                       //it returns a viewHolder
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.reports_recycle_item, parent, false);   //to change the list_item layout to a view
        MyReportsListAdapter.ViewHolder viewHolder = new MyReportsListAdapter.ViewHolder(listItem); //to make a viewHolder


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyReportsListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) { //holder is an object of ViewHolder class
        holder.toText.setText(listdata.get(position).getEnd());  //set day of item to textView for day
        holder.fromText.setText(listdata.get(position).getStart());  //set day of item to textView for day

        holder.exclamationImage.setOnClickListener(new View.OnClickListener() {  //to open vacationReportdialog by clicking item
            @Override
            public void onClick(View view) {
                descriptionTextView.setVisibility(View.VISIBLE);  //to make text "توضیحات " visisble
                descriptionText.setVisibility(View.VISIBLE);      //to make descriptionTextView visible
                descriptionTextView.setText(listdata.get(position).getDescription());    //to set description in description textview
            }
        });
    }
    @Override
    public int getItemCount() {  //returns the number of items (of type MyListData) in list data
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fromText;
        public ImageView exclamationImage;       //to poise type of vacation
        public TextView toText;   //to poise status of vacation

        public ViewHolder(View itemView) {
            super(itemView);
            this.fromText =  itemView.findViewById(R.id.time_from);
            this.exclamationImage=itemView.findViewById(R.id.exclamation);
            this.toText= itemView.findViewById(R.id.time_to);

        }
    }
}
