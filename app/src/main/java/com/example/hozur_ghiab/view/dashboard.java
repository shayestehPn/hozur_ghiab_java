package com.example.hozur_ghiab.view;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hozur_ghiab.model.MyListData;
import com.example.hozur_ghiab.my_class.MyListAdapter;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.example.hozur_ghiab.view_model.DashboardViewModel;
import com.example.hozur_ghiab.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class dashboard extends Fragment {

    private DashboardViewModel mViewModel;
    View view;
    Button enterButton,exitButton;
    SharedPrefManager sharedPrefManager;
    LinearLayout ReportsText,dateOfToday,enterTime,recycleLayout;
    RecyclerView reportsRecycle;
     Toast m_currentToast;
    TextView dateText,enterTimeText;
    EditText writeDescription;
    ImageView hamburgerIcon;
    List<MyListData> list;
    CustomProgressDialog progress ;
    int count=0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.dashboard_fragment, container, false);
        myInit();
        listener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        progress.show();
        mViewModel.message.setValue("");
        mViewModel.getDashboard(getContext(),getActivity());   //to get dashboard and check if user has entered or exited ,etc




        mViewModel.hasEntered.observe(getActivity(),state->{
            if (mViewModel.hasEntered.getValue()) {      //so the user has entered
                loadExitDashboard();                                   //to show exit dashboard if user has entered
            }
            else{
                loadEnterDashboard();                             //to show enter dashboard if user has not entered
            }
        });

        mViewModel.progressFinished.observe(getActivity(),state->{     //to make progressbar invisible if we get response from server
            progress.dismiss();
        });

        mViewModel.message.observe(getActivity(),state->{     //to show ToastMessage if needed
            if(!mViewModel.message.getValue().equals("")){
            progress.dismiss();
            showToast(mViewModel.message.getValue());}
            else{

            }

        });

        mViewModel.reportsList.observe(getActivity(),state->{     //to set the recyclerView
            addToRecycle(mViewModel.reportsList.getValue());
        });

        mViewModel.name.observe(getActivity(),state->{
            ((MainActivity) getActivity()).setUserName(mViewModel.name.getValue());
        });
    }

    public void myInit(){                //to initialize variables
        enterButton=view.findViewById(R.id.enter_button);
        exitButton=view.findViewById(R.id.exit_button);
        sharedPrefManager=new SharedPrefManager(getActivity());
        ReportsText=view.findViewById(R.id.reports_text);
        dateOfToday=view.findViewById(R.id.date_of_today);
        enterTime=view.findViewById(R.id.enter_time);
        reportsRecycle=view.findViewById(R.id.reports_recycle);
        dateText=view.findViewById(R.id.date_text);
        enterTimeText=view.findViewById(R.id.enter_time_text);
        writeDescription=view.findViewById(R.id.write_description);
        recycleLayout=view.findViewById(R.id.recycle_layout);
        hamburgerIcon=view.findViewById(R.id.hamburger_icon);

        progress = new CustomProgressDialog(getActivity());       //my customProgressDialog to show progress
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    //to set style for progress dialog

        list=new ArrayList<>();
    }

    public void makeGone(View myView){      //to make view gone
        myView.setVisibility(View.GONE);
    }
    public void makeInvisible(View myView){
        myView.setVisibility(View.INVISIBLE);
    }

    public void makeVisible(View myView){       //to make view visible
        myView.setVisibility(View.VISIBLE);
    }

    public void loadExitDashboard(){       //to load exit dashboard
        writeDescription.setText("");
        dateText.setText(mViewModel.date);                     //to set date of today in textView
        enterTimeText.setText(mViewModel.enterTime);           //to set time of enterance in textView
        makeGone(enterButton);
        makeVisible(ReportsText);
        makeVisible(dateOfToday);
        makeVisible(enterTime);
        makeVisible(writeDescription);
        makeVisible(reportsRecycle);
        makeVisible(exitButton);
    }

    public void loadEnterDashboard(){       //to load enter dashboard
        makeVisible(enterButton);
        makeInvisible(ReportsText);
        makeGone(dateOfToday);
        makeGone(enterTime);
        makeGone(writeDescription);
        makeGone(exitButton);
        writeDescription.setText("");
    }

    public void showToast(String text)
    {
        if(!text.equals("")){
        if(m_currentToast != null)
        {
            m_currentToast.cancel();                 //not to show Toast message for several times
        }
        m_currentToast = Toast.makeText(getActivity(), text,
                Toast.LENGTH_SHORT);
        m_currentToast.show();                       //to show Toast message;
            }
    }

    public void addToRecycle(List<MyListData> list){
        MyListAdapter adapter=new MyListAdapter(list,getActivity());      //to make an object of MyListAdapter type with input : the list of items we made
        reportsRecycle.setAdapter(adapter);                         //set the adapter to the RecyclerView
        reportsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));    //to make the components of the list appear linear and  vertical(it is vertical  by default)
    }


    public void listener(){
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                mViewModel.enterUser(getContext(), getActivity());        //to enter user
                exitButton.setEnabled(true);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                if(mViewModel.haveDescription(writeDescription.getText().toString())){
                mViewModel.exitUser(getContext(),getActivity(),writeDescription.getText().toString());    //to exit user
                    if(mViewModel.errorMessage.equals("")) {
                        exitButton.setEnabled(false);
                    }
                    if(mViewModel.errorMessage.equals( "شما زمان اتمام نشده ندارید")) {
                        loadEnterDashboard();
                    }
                }
            }
        });

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {     //to open navigation drawer when we click the hamburger icon
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openDrawer();        //to open  Navigation Drawer
            }
        });
    }
}