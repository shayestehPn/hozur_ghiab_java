package com.example.hozur_ghiab.my_class;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.MyListData;
import com.example.hozur_ghiab.view.DescriptionDialog;

import java.util.ArrayList;
import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> { //adapter for dashboard recyclerView
    DescriptionDialog myDialog;
    private List<MyListData> listdata=new ArrayList<>();      //the list of items of type MyListData
    Activity activity;

    public MyListAdapter(List<MyListData> listdata, Activity a) {  //constructor
        this.listdata = listdata;
        activity=a;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {                       //it returns a viewHolder
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.dashboard_recycle_list_item, parent, false);   //to change the list_item layout to a view
        ViewHolder viewHolder = new ViewHolder(listItem); //to make a viewHolder with input : listItem View( list_item xml layout that changed to view)
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(position==listdata.size()-1){
            holder.linearLayout.setBackgroundResource(R.drawable.border_13);
        }
        if(listdata.get(position).getDescription().length()<=13)
        holder.descriptionText.setText(listdata.get(position).getDescription());             //holder is an object of ViewHolder class that has 3 text views one for description and others for enterTime and exitTime and this sets the description of item to holder
        else{
            holder.descriptionText.setText(listdata.get(position).getDescription().subSequence(0,10)+"...");             //holder is an object of ViewHolder class that has 3 text views one for description and others for enterTime and exitTime and this sets the description of item to holder
        }
        holder.exitText.setText(String.valueOf(listdata.get(position).getExitTime()));   //to set the exitTime of item of listdata of type MyListData to holder
        holder.enterText.setText(String.valueOf(listdata.get(position).getEnterTime()));   //to set the enterTime of item of listdata of type MyListData to holder
        holder.descriptionText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new DescriptionDialog(activity,listdata.get(position).getDescription() );     //an object of CustomDialog class to show description
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(R.drawable.error_dialog_shape));      //set style for errorDialog
                if(!listdata.get(position).getDescription().equals("..."))
                myDialog.show();        //to show dialog
            }
        });
    }
    @Override
    public int getItemCount() {  //returns the number of items (of type MyListData) in list data
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView exitText;       //to poise exitTime of user
        public TextView descriptionText;       //to poise description of user
        public TextView enterText;   //to poise enterTime of user
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.descriptionText =  itemView.findViewById(R.id.description);
            this.exitText =  itemView.findViewById(R.id.exit);
            this.enterText= itemView.findViewById(R.id.enter);
            this.linearLayout=itemView.findViewById(R.id.linearLayout);
        }
    }
}
