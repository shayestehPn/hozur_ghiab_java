package com.example.hozur_ghiab.my_class;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.MyVacationListData;
import com.example.hozur_ghiab.view.DescriptionDialog;
import com.example.hozur_ghiab.view.VacationReportDialog;

import java.util.ArrayList;
import java.util.List;

public class MyVacationListAdapter extends RecyclerView.Adapter<MyVacationListAdapter.ViewHolder> {

    private List<MyVacationListData> listdata=new ArrayList<>();      //the list of items of type MyVacationListData
    Activity activity;
    public MyVacationListAdapter(List<MyVacationListData> listdata, Activity a) {  //constructor
        this.listdata = listdata;
        activity=a;
    }

    @Override
    public MyVacationListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {                       //it returns a viewHolder
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.vacation_recycle_list_item, parent, false);   //to change the list_item layout to a view
        MyVacationListAdapter.ViewHolder viewHolder = new MyVacationListAdapter.ViewHolder(listItem); //to make a viewHolder
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(MyVacationListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) { //holder is an object of ViewHolder class
        holder.dayText.setText(listdata.get(position).getDay());  //set day of item to textView for day
        holder.typeImage.setBackground(listdata.get(position).getTypeBackground());  //set vacationType of item to ImageView for type

        //set status of item to textView for type
        if(listdata.get(position).getStatus().equals("requested")){
            holder.statusText.setTextColor(Color.parseColor("#707070"));  //gray
            holder.statusText.setText("درخواست");
        }
        else if(listdata.get(position).getStatus().equals("applied")){
            holder.statusText.setTextColor(Color.parseColor("#00AD35"));   //green
            holder.statusText.setText("تایید شده");
        }
        else if(listdata.get(position).getStatus().equals("not_verified")){
            holder.statusText.setTextColor(Color.parseColor("#FF2222"));   //red
            holder.statusText.setText("رد شده");
        }


        holder.vacationRecycleItemLayout.setOnClickListener(new View.OnClickListener() {  //to open vacationReportdialog by clicking item
            @Override
            public void onClick(View view) {
                VacationReportDialog myDialog=new VacationReportDialog(activity,listdata.get(position).getTitle(),listdata.get(position).getCreatedAt()
                        ,listdata.get(position).getStatus(),listdata.get(position).getDescription(),listdata.get(position).getDetails());

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));  //to set style for dialog

                myDialog.show();  //to show dialog
            }
        });
    }
    @Override
    public int getItemCount() {  //returns the number of items (of type MyListData) in list data
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayText;       //to poise day of vacation
        public ImageView typeImage;       //to poise type of vacation
        public TextView statusText;   //to poise status of vacation
        public ImageView leftImage;   //the imageView in list to show report
        public LinearLayout vacationRecycleItemLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            this.dayText =  itemView.findViewById(R.id.day);
            this.typeImage =  itemView.findViewById(R.id.type);
            this.statusText= itemView.findViewById(R.id.status);
            this.leftImage=itemView.findViewById(R.id.left);
            this.vacationRecycleItemLayout=itemView.findViewById(R.id.vacation_recycle_item_layout);
        }
    }
}
