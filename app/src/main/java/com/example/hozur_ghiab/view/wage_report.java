package com.example.hozur_ghiab.view;

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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.model.Month;
import com.example.hozur_ghiab.model.MyWageListData;
import com.example.hozur_ghiab.my_class.CustomMonthSpinnerAdapter;
import com.example.hozur_ghiab.my_class.CustomYearSpinnerAdapter;
import com.example.hozur_ghiab.my_class.MyVacationListAdapter;
import com.example.hozur_ghiab.my_class.MyWageListAdapter;
import com.example.hozur_ghiab.view_model.WageReportViewModel;

import java.util.ArrayList;
import java.util.List;

public class wage_report extends Fragment implements AdapterView.OnItemSelectedListener {

    private WageReportViewModel mViewModel;
    View view;

    RecyclerView wageReportsRecycle;    //recyclerView to show wage  reports

    List<MyWageListData> list;          //list to save reports

    ImageView hamburgerIcon;                //icon for opening navigation drawer

    Spinner monthSpinner,yearSpinner;       //spinner for selecting month and year

    CustomMonthSpinnerAdapter monthAdapter,yearAdapter;   //adapters for monthSpinner and yearSpinner

    ArrayList<Month> myMonths;         //ArrayList to save months
    ArrayList<String>myYears;                 //ArrayList to save years

    Boolean isFirstTimeMonth=true;         //is true if we have not selected from month spinner yet
    Boolean isFirstTimeYear=true;          //is true if we have not selected from year spinner yet

    TextView defaultRecyclerItem;          //textView to show in list if we dont have any item in recyclerView

    CustomProgressDialog progress ;
    LinearLayout wageReportRecycleHeader ;     //header of recycler view

    int count=0;   //not to send request for 2 times

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.wage_report_fragment, container, false);
        myInit();
        listener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WageReportViewModel.class);

        progress.show();      //to show progress until list is available
        mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.selectedMonth.getValue());  //to get wage reports
        count++;

        mViewModel.reportsList.observe(getActivity(),state->{    //to add reports to recycler View
            addToRecycle(mViewModel.reportsList.getValue());
        });

        mViewModel.progressFinished.observe(getActivity(),state->{     //to make progressbar invisible if we get response from server
            progress.dismiss();
        });

        mViewModel.haveMonths.observe(getActivity(),state->{
            if(mViewModel.haveMonths.getValue()){       //if we get months from server we can save them and set them to monthSpinner
                setMonths();
                setMonthSpinner();
            }
        });

        mViewModel.haveYears.observe(getActivity(),state->{
            if(mViewModel.haveYears.getValue()){        //if we get years from server we can save them and set them to yearSpinner
                setYears();
                setYearSpinner();
            }
        });

        mViewModel.selectedYear.observe(getActivity(),state->{
            if(count>=2) {
                if (mViewModel.selectedMonth.getValue() == "") {    //so user has not selected the month and has selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.activeMonth);
                } else                                             //so user has selected the month and has selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.selectedMonth.getValue());
            }
            else{
                count++;
            }
        });

        mViewModel.selectedMonth.observe(getActivity(),state->{
            if(count>=2) {
                if (mViewModel.selectedYear.getValue() == "")      //so user has  selected the month and has not  selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.activeYear, mViewModel.selectedMonth.getValue());
                else                                              //so user has  selected the month and has  selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.selectedMonth.getValue());
            }
            else {
                count++;
            }
        });
    }
    public void myInit(){
        wageReportsRecycle=view.findViewById(R.id.wage_reports_recycle);
        hamburgerIcon = view.findViewById(R.id.hamburger_icon);

        progress = new CustomProgressDialog(getActivity());       //my customProgressDialog to show progress
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    //to set style for progress dialog

        list=new ArrayList<>();

        defaultRecyclerItem=view.findViewById(R.id.default_recycler_item);

        wageReportRecycleHeader=view.findViewById(R.id.wage_report_recycle_header);

    }
    public void listener(){
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {         //to open navigation drawer when we click the hamburger icon
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openDrawer();        //to open Navigation Drawer
            }
        });
    }

    public void setMonths(){           //to save months we got from server
        myMonths= mViewModel.months;
    }

    public void setYears(){              //to save years we got from server
        myYears=new ArrayList<>();
        for(int i=0;i<mViewModel.years.size();i++){
            myYears.add(mViewModel.years.get(i).toString());
        }
    }
    public void setMonthSpinner(){

        Spinner monthSpinner = view.findViewById(R.id.month_spinner);
        CustomMonthSpinnerAdapter monthAdapter=new CustomMonthSpinnerAdapter(getContext(),myMonths);  //adapter for spinner

        //to set style for spinner
        monthSpinner.setPopupBackgroundDrawable(new ColorDrawable(Color.alpha(1)));

        //to set active month for month spinner
        monthSpinner.post(new Runnable() {
            @Override
            public void run() {
                monthSpinner.setSelection(Integer.parseInt(mViewModel.activeMonth)-1);
            }
        });

        monthSpinner.setOnItemSelectedListener(       //monthSpinner onItemSelected
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {

                        // It returns the clicked item.
                        Month clickedItem = (Month)
                                parent.getItemAtPosition(position);


                        if(isFirstTimeMonth){    //if it is firstTime we open the fragment do nothing (not to select first items of spinner by default)
                            isFirstTimeMonth = false;
                            return;
                        }
                        else{
                            progress.show();
                            mViewModel.selectedMonth.setValue( String.valueOf(clickedItem.getValue()));  //save the month selected by user as selectedMonth
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });
        monthSpinner.setAdapter(monthAdapter);   //set adapter for spinner
    }


    public void setYearSpinner(){

        Spinner yearSpinner = view.findViewById(R.id.year_spinner);
        CustomYearSpinnerAdapter yearAdapter=new CustomYearSpinnerAdapter(getContext(),myYears);  //adapter for spinner

        //to set style for spinner
        yearSpinner.setPopupBackgroundDrawable(new ColorDrawable(Color.alpha(1)));


        yearSpinner.post(new Runnable() {
            @Override
            public void run() {
                yearSpinner.setSelection(myYears.size()-1);
            }
        });
        yearSpinner.setOnItemSelectedListener(      //yearSpinner onItemSelected
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id)
                    {
                        // It returns the clicked item.
                        String clickedItem = (String)
                                parent.getItemAtPosition(position);

                        if(isFirstTimeYear){  //if it is firstTime we open the fragment do nothing (not to select first items of spinner by default)
                            isFirstTimeYear = false;
                            return;
                        }
                        else{
                            progress.show();
                            mViewModel.selectedYear.setValue(clickedItem);   //save the year selected by user as selectedYear
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent)
                    {
                    }
                });
        yearSpinner.setAdapter(yearAdapter);     //set adapter for year spinner
    }

    public void addToRecycle(List<MyWageListData> list){   //to add wage reports to recyclerView

        if(list.isEmpty()){    //if list is empty so we shoud show the textView that say list is empty
            wageReportsRecycle.setVisibility(View.GONE);
            defaultRecyclerItem.setVisibility(View.VISIBLE);
            wageReportRecycleHeader.setVisibility(View.GONE);
        }
        else{     //if list is not empty add the items to recyclerView
            wageReportsRecycle.setVisibility(View.VISIBLE);
            defaultRecyclerItem.setVisibility(View.GONE);
            wageReportRecycleHeader.setVisibility(View.VISIBLE);

            MyWageListAdapter adapter=new MyWageListAdapter(list,getActivity());      //to make an object of MyWageListAdapter type with input : the list of items we made
            wageReportsRecycle.setAdapter(adapter);                         //set the adapter to the RecyclerView
            wageReportsRecycle.setLayoutManager(new LinearLayoutManager(getContext()));    //to make the components of the list appear linear and  vertical(it is vertical  by default)
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

}