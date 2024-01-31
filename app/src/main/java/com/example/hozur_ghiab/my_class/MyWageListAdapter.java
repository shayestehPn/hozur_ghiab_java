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

import com.example.hozur_ghiab.model.MyWageListData;
import com.example.hozur_ghiab.view.DescriptionDialog;
import com.example.hozur_ghiab.view.WageReportDialog;

import java.util.ArrayList;
import java.util.List;


    public class  MyWageListAdapter extends RecyclerView.Adapter<MyWageListAdapter.ViewHolder> {

        private List<MyWageListData> listdata = new ArrayList<>();      //the list of items of type MyWageListData
        Activity activity;

        public MyWageListAdapter(List<MyWageListData> listdata, Activity a) {  //constructor
            this.listdata = listdata;
            activity = a;
        }

        @Override
        public MyWageListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {                       //it returns a viewHolder
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.wage_recycle_list_item, parent, false);   //to change the list_item layout to a view
            MyWageListAdapter.ViewHolder viewHolder = new MyWageListAdapter.ViewHolder(listItem); //to make a viewHolder
            return viewHolder;
        }


        @Override
        public void onBindViewHolder(com.example.hozur_ghiab.my_class.MyWageListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) { //holder is an object of ViewHolder class
            holder.statusImage.setBackground(listdata.get(position).getstatusBackground());  //to set status for status image view
            if(listdata.get(position).getValue().length()>6){
                holder.valueText.setText(listdata.get(position).getValue().substring(0,3)+"...");  //to set value for value textview
            }
            else{
                holder.valueText.setText(listdata.get(position).getValue());
            }
            if(listdata.get(position).getDescription().length()>6){
                holder.descriptionText.setText(listdata.get(position).getDescription().substring(0,3)+"...");  //to set description for description text view
            }
            else{
                holder.descriptionText.setText(listdata.get(position).getDescription());
            }

            holder.typeText.setText(listdata.get(position).getType());  //to set type for type textview

            holder.wageRecycleItemLayout.setOnClickListener(new View.OnClickListener() {  //to open WageReportdialog by clicking item
                @Override
                public void onClick(View view) {
                    WageReportDialog myDialog = new WageReportDialog(activity, listdata.get(position).getValue(), listdata.get(position).getType()
                            , listdata.get(position).getStatus(), listdata.get(position).getCreatedAt(), listdata.get(position).getDescription());

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

            public TextView valueText;       //to poise value of wage
            public ImageView statusImage;       //to poise status of wage
            public TextView typeText;   //to poise type of wage
            public TextView descriptionText;   //to poise description of wage
            public ImageView leftImage;   //the imageView in list to show report
            public LinearLayout wageRecycleItemLayout;  //linearlayout of item

            public ViewHolder(View itemView) {  //to initialize values
                super(itemView);
                this.valueText = itemView.findViewById(R.id.value);
                this.statusImage = itemView.findViewById(R.id.status);
                this.typeText = itemView.findViewById(R.id.type);
                this.leftImage = itemView.findViewById(R.id.left);
                this.descriptionText = itemView.findViewById(R.id.description);
                this.wageRecycleItemLayout = itemView.findViewById(R.id.wage_recycle_item_layout);
            }
        }
    }

