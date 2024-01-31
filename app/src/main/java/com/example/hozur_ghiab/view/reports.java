package com.example.hozur_ghiab.view;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.example.hozur_ghiab.my_class.Assistance;
import com.example.hozur_ghiab.my_class.CustomMonthSpinnerAdapter;
import com.example.hozur_ghiab.my_class.CustomYearSpinnerAdapter;
import com.example.hozur_ghiab.view_model.ReportsViewModel;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class reports extends Fragment {

    private ReportsViewModel mViewModel;
    ImageView hamburgerIcon;
    View view;
    Spinner monthSpinner,yearSpinner;       //spinner for selecting month and year


    ArrayList<Month> myMonths;         //ArrayList to save months
    ArrayList<String>myYears;                 //ArrayList to save years

    Boolean isFirstTimeMonth=true;         //is true if we have not selected from month spinner yet
    Boolean isFirstTimeYear=true;          //is true if we have not selected from year spinner yet

    CustomProgressDialog progress ;

    LinearLayout calenderLastLine,emptyLayOut;

    int yearsSize;
    int monthSize;

    TextView totalTime,vacationTime,extraTime,delayTime,usdProfit,irrProfit,totalTimeText,vacationTimeText,delayTimeText;

    TextView one,two,second,three,four,five,six,seven,eight,nine,ten,eleven,twelve,thirteen,fourteen,fifteen,sixteen,seventeen,
    eighteen,nineteen,twenty,twentyOne,twentyTwo,twentyThree,twentyFour,twentyFive,twentySix,twentySeven,twentyEight,twentyNine,thirty,
    thirtyOne,

    firstEmpty,secondEmpty,thirdEmpty,fourthEmpty,fifthEmpty,sixthEmpty;




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       view= inflater.inflate(R.layout.reports_fragment, container, false);
       myInit();
       listener();
       return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ReportsViewModel.class);

        progress.show();      //to show progress until list is available

        mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.selectedMonth.getValue());  //to get wage reports

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

        mViewModel.haveData.observe(getActivity(),state->{
            if(mViewModel.haveData.getValue()){       //if we get data from server we can show
                setData();
            }
        });

        mViewModel.selectedYear.observe(getActivity(),state->{

                if (mViewModel.selectedMonth.getValue() == "") {    //so user has not selected the month and has selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.activeMonth);
                } else {                                            //so user has selected the month and has selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.selectedMonth.getValue());
                }
        });

        mViewModel.selectedMonth.observe(getActivity(),state->{

                if (mViewModel.selectedYear.getValue() == "") {     //so user has  selected the month and has not  selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.activeYear, mViewModel.selectedMonth.getValue());}
                else {                                    //so user has  selected the month and has  selected year
                    mViewModel.getReports(getContext(), getActivity(), mViewModel.selectedYear.getValue(), mViewModel.selectedMonth.getValue());
                }
        });
    }

    public void myInit(){
        totalTime=view.findViewById(R.id.total_time);
        vacationTime=view.findViewById(R.id.vacation_time);
        extraTime=view.findViewById(R.id.extra_time);
        delayTime=view.findViewById(R.id.delay_time);
        usdProfit=view.findViewById(R.id.usd_profit);
        irrProfit=view.findViewById(R.id.irr_profit);
        hamburgerIcon=view.findViewById(R.id.hamburger_icon);
        totalTimeText=view.findViewById(R.id.total_time_text);
        vacationTimeText=view.findViewById(R.id.vacation_time_text);
        delayTimeText=view.findViewById(R.id.delay_time_text);
        one=view.findViewById(R.id.one);
        two=view.findViewById(R.id.two);
        three=view.findViewById(R.id.three);
        four=view.findViewById(R.id.four);
        five=view.findViewById(R.id.five);
        six=view.findViewById(R.id.six);
        seven=view.findViewById(R.id.seven);
        eight=view.findViewById(R.id.eight);
        nine=view.findViewById(R.id.nine);
        ten=view.findViewById(R.id.ten);
        eleven=view.findViewById(R.id.eleven);
        twelve=view.findViewById(R.id.twelve);
        thirteen=view.findViewById(R.id.thirteen);
        fourteen=view.findViewById(R.id.fourteen);
        fifteen=view.findViewById(R.id.fifteen);
        sixteen=view.findViewById(R.id.sixteen);
        seventeen=view.findViewById(R.id.seventeen);
        eighteen=view.findViewById(R.id.eighteen);
        nineteen=view.findViewById(R.id.nineteen);
        twenty=view.findViewById(R.id.twenty);
        twentyOne=view.findViewById(R.id.twenty_one);
        twentyTwo=view.findViewById(R.id.twenty_two);
        twentyThree=view.findViewById(R.id.twenty_three);
        twentyFour=view.findViewById(R.id.twenty_four);
        twentyFive=view.findViewById(R.id.twenty_five);
        twentySix=view.findViewById(R.id.twenty_six);
        twentySeven=view.findViewById(R.id.twenty_seven);
        twentyEight=view.findViewById(R.id.twenty_eight);
        twentyNine=view.findViewById(R.id.twenty_nine);
        thirty=view.findViewById(R.id.thirty);
        thirtyOne=view.findViewById(R.id.thirty_one);

        firstEmpty=view.findViewById(R.id.first_empty);
        secondEmpty=view.findViewById(R.id.second_empty);
        thirdEmpty=view.findViewById(R.id.third_empty);
        fourthEmpty=view.findViewById(R.id.forth_empty);
        fifthEmpty=view.findViewById(R.id.fifth_empty);
        sixthEmpty=view.findViewById(R.id.sixth_empty);

        calenderLastLine=view.findViewById(R.id.last_line);
        emptyLayOut=view.findViewById(R.id.empty_layout);

        progress = new CustomProgressDialog(getActivity());       //my customProgressDialog to show progress
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    //to set style for progress dialog
    }

    public void listener(){
        hamburgerIcon.setOnClickListener(new View.OnClickListener() {         //to open navigation drawer when we click the hamburger icon
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openDrawer();        //to open Navigation Drawer
            }
        });
        calenderOnclick(one);
        calenderOnclick(two);
        calenderOnclick(three);
        calenderOnclick(four);
        calenderOnclick(five);
        calenderOnclick(six);
        calenderOnclick(seven);
        calenderOnclick(eight);
        calenderOnclick(nine);
        calenderOnclick(ten);
        calenderOnclick(eleven);
        calenderOnclick(twelve);
        calenderOnclick(thirteen);
        calenderOnclick(fourteen);
        calenderOnclick(fifteen);
        calenderOnclick(sixteen);
        calenderOnclick(seventeen);
        calenderOnclick(eighteen);
        calenderOnclick(nineteen);
        calenderOnclick(twenty);
        calenderOnclick(twentyOne);
        calenderOnclick(twentyTwo);
        calenderOnclick(twentyThree);
        calenderOnclick(twentyFour);
        calenderOnclick(twentyFive);
        calenderOnclick(twentySix);
        calenderOnclick(twentySeven);
        calenderOnclick(twentyEight);
        calenderOnclick(twentyNine);
        calenderOnclick(thirty);
        calenderOnclick(thirtyOne);
        calenderOnclick(firstEmpty);
        calenderOnclick(secondEmpty);
        calenderOnclick(thirdEmpty);
        calenderOnclick(fourthEmpty);
        calenderOnclick(fifthEmpty);
        calenderOnclick(sixthEmpty);
    }


    public void calenderOnclick(TextView myTextView){
        myTextView.setOnClickListener(new View.OnClickListener() {         //to open navigation drawer when we click the hamburger icon
            @Override
            public void onClick(View view) {
                CustomReportsDialog myReportsDialog=new CustomReportsDialog(getActivity(),mViewModel.itemsList.get(Integer.parseInt(myTextView.getText().toString())-1));
                myReportsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));
                myReportsDialog.show();
            }
        });
    }

    public void makeHollidayRed(TextView myTextView){  //make border of  textView of holliday red
        if(!defineTextViewNumber(myTextView).equals("0")) {
            if (Integer.parseInt(defineTextViewNumber(myTextView)) - 1 < mViewModel.itemsList.size()) {
                if (!mViewModel.itemsList.get(Integer.parseInt(defineTextViewNumber(myTextView)) - 1).getHollidayDescription().equals("")) {
                    myTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.square_red));
                }
            }
        }
    }




    public void setMonths(){           //to save months we got from server
        myMonths= mViewModel.months;
        monthSize=myMonths.size();
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
        monthAdapter.setReportsSpinner();


        //to set style for spinner
        monthSpinner.setPopupBackgroundDrawable(new ColorDrawable(Color.alpha(1)));

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
                        Month clickedItem = (Month)
                                parent.getItemAtPosition(position);
                        if (isFirstTimeMonth) {  //if it is firstTime we open the fragment do nothing (not to select first items of spinner by default)
                            isFirstTimeMonth = false;
                            return;
                        }
                        // It returns the clicked item.
                        else {
                            progress.show();
                            mViewModel.selectedMonth.setValue(String.valueOf(clickedItem.getValue()));   //save the year selected by user as selectedYear
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
        yearAdapter.setReportsSpinner();

        yearSpinner.post(new Runnable() {
            @Override
            public void run() {
                yearSpinner.setSelection(myYears.size()-1);
            }
        });

        //to set style for spinner
        yearSpinner.setPopupBackgroundDrawable(new ColorDrawable(Color.alpha(1)));

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
                        else {
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

    public void setNumberText(TextView myTextView,String minusNumber){
        myTextView.setText(mViewModel.minus(myTextView.getText().toString(),minusNumber));
        if (myTextView.getText().equals(""))
            myTextView.setVisibility(View.INVISIBLE);
    }

    public void setTextViewDefaultState(TextView textView,String number) {
        textView.setText(number);
        textView.setVisibility(View.VISIBLE);
    }

    public void setEmptyTextViewDefaultState(TextView textView,String number) {
        textView.setText(number);
        textView.setVisibility(View.INVISIBLE);
    }

    public void makeTodayFilled(TextView myTextView){  //make textView for today filled

         final SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(System.currentTimeMillis());
        String shamsiDate=Assistance.convertDateToShamsi(yearFormat.format(date).toString());

        String day=shamsiDate.substring(8);

        if(defineTextViewNumber(myTextView).equals(day)){

            if((mViewModel.selectedMonth.getValue().toString().equals("") && mViewModel.selectedYear.getValue().toString().equals("")) ||
                    mViewModel.selectedMonth.getValue().toString().equals("") && defineSelectedVelue(mViewModel.selectedYear.getValue().toString())==(defineSelectedVelue(mViewModel.activeYear)) ||
                   defineSelectedVelue( mViewModel.selectedMonth.getValue().toString())==(Integer.parseInt(mViewModel.activeMonth)) && mViewModel.selectedYear.getValue().toString().equals("") ||
                    defineSelectedVelue( mViewModel.selectedMonth.getValue().toString())==(defineSelectedVelue(mViewModel.activeMonth)) && defineSelectedVelue(mViewModel.selectedYear.getValue().toString())==(defineSelectedVelue(mViewModel.activeYear))
            ) {
                myTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.square_filled));
                myTextView.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    public int defineSelectedVelue(String value){
        if (value.toString().equals(""))
            return 0;
            else{
                return Integer.parseInt(value);
        }
    }

    public void setTextViewDefaultBorder(TextView myTextView){   //to set blue border for textView
        myTextView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.square_blue));
    }

    public void setTextViewDefaultTextColor(TextView myTextView){   //to set textcolor default textColor black
        myTextView.setTextColor(Color.BLACK);
    }



    public void setTextViewBorder(TextView myTextView){   //to set border of textView red if its holliday and make textView filled if its today
        makeHollidayRed(myTextView);
        makeTodayFilled(myTextView);
    }

    public String defineTextViewNumber(TextView myTextView){  //to define number of textViews of calender
        if(!myTextView.getText().toString().equals("")){
            return myTextView.getText().toString();
        }
        else{
            return "0";
        }
    }


    public void setDefaultCalender(){
        setTextViewDefaultState(one,"1");   //to set ont text to 1 and make it visible
        setTextViewDefaultBorder(one);
        setTextViewDefaultTextColor(one);
        setTextViewDefaultState(two,"2");
        setTextViewDefaultBorder(two);
        setTextViewDefaultTextColor(two);
        setTextViewDefaultState(three,"3");
        setTextViewDefaultBorder(three);
        setTextViewDefaultTextColor(three);
        setTextViewDefaultState(four,"4");
        setTextViewDefaultBorder(four);
        setTextViewDefaultTextColor(four);
        setTextViewDefaultState(five,"5");
        setTextViewDefaultBorder(five);
        setTextViewDefaultTextColor(five);
        setTextViewDefaultState(six,"6");
        setTextViewDefaultBorder(six);
        setTextViewDefaultTextColor(six);
        setTextViewDefaultState(seven,"7");
        setTextViewDefaultBorder(seven);
        setTextViewDefaultTextColor(seven);
        setTextViewDefaultState(eight,"8");
        setTextViewDefaultBorder(eight);
        setTextViewDefaultTextColor(eight);
        setTextViewDefaultState(nine,"9");
        setTextViewDefaultBorder(nine);
        setTextViewDefaultTextColor(nine);
        setTextViewDefaultState(ten,"10");
        setTextViewDefaultBorder(ten);
        setTextViewDefaultTextColor(ten);
        setTextViewDefaultState(eleven,"11");
        setTextViewDefaultBorder(eleven);
        setTextViewDefaultTextColor(eleven);
        setTextViewDefaultState(twelve,"12");
        setTextViewDefaultBorder(twelve);
        setTextViewDefaultTextColor(twelve);
        setTextViewDefaultState(thirteen,"13");
        setTextViewDefaultBorder(thirteen);
        setTextViewDefaultTextColor(thirteen);
        setTextViewDefaultState(fourteen,"14");
        setTextViewDefaultBorder(fourteen);
        setTextViewDefaultTextColor(fourteen);
        setTextViewDefaultState(fifteen,"15");
        setTextViewDefaultBorder(fifteen);
        setTextViewDefaultTextColor(fifteen);
        setTextViewDefaultState(sixteen,"16");
        setTextViewDefaultBorder(sixteen);
        setTextViewDefaultTextColor(sixteen);
        setTextViewDefaultState(seventeen,"17");
        setTextViewDefaultBorder(seventeen);
        setTextViewDefaultTextColor(seventeen);
        setTextViewDefaultState(eighteen,"18");
        setTextViewDefaultBorder(eighteen);
        setTextViewDefaultTextColor(eighteen);
        setTextViewDefaultState(nineteen,"19");
        setTextViewDefaultBorder(nineteen);
        setTextViewDefaultTextColor(nineteen);
        setTextViewDefaultState(twenty,"20");
        setTextViewDefaultBorder(twenty);
        setTextViewDefaultTextColor(twenty);
        setTextViewDefaultState(twentyOne,"21");
        setTextViewDefaultBorder(twentyOne);
        setTextViewDefaultTextColor(twentyOne);
        setTextViewDefaultState(twentyTwo,"22");
        setTextViewDefaultBorder(twentyTwo);
        setTextViewDefaultTextColor(twentyTwo);
        setTextViewDefaultState(twentyThree,"23");
        setTextViewDefaultBorder(twentyThree);
        setTextViewDefaultTextColor(twentyThree);
        setTextViewDefaultState(twentyFour,"24");
        setTextViewDefaultBorder(twentyFour);
        setTextViewDefaultTextColor(twentyFour);
        setTextViewDefaultState(twentyFive,"25");
        setTextViewDefaultBorder(twentyFive);
        setTextViewDefaultTextColor(twentyFive);
        setTextViewDefaultState(twentySix,"26");
        setTextViewDefaultBorder(twentySix);
        setTextViewDefaultTextColor(twentySix);
        setTextViewDefaultState(twentySeven,"27");
        setTextViewDefaultBorder(twentySeven);
        setTextViewDefaultTextColor(twentySeven);
        setTextViewDefaultState(twentyEight,"28");
        setTextViewDefaultBorder(twentyEight);
        setTextViewDefaultTextColor(twentyEight);
        setTextViewDefaultState(twentyNine,"29");
        setTextViewDefaultBorder(twentyNine);
        setTextViewDefaultTextColor(twentyNine);
        setTextViewDefaultState(thirty,"30");
        setTextViewDefaultBorder(thirty);
        setTextViewDefaultTextColor(thirty);
        setTextViewDefaultState(thirtyOne,"31");
        setTextViewDefaultBorder(thirtyOne);
        setTextViewDefaultTextColor(thirtyOne);

        setEmptyTextViewDefaultState(firstEmpty,"26");   //to set firstEmpty text to 26 and make it invisible
        setTextViewDefaultBorder(firstEmpty);
        setTextViewDefaultTextColor(firstEmpty);
        setEmptyTextViewDefaultState(secondEmpty,"27");
        setTextViewDefaultBorder(secondEmpty);
        setTextViewDefaultTextColor(secondEmpty);
        setEmptyTextViewDefaultState(thirdEmpty,"28");
        setTextViewDefaultBorder(thirdEmpty);
        setTextViewDefaultTextColor(thirdEmpty);
        setEmptyTextViewDefaultState(fourthEmpty,"29");
        setTextViewDefaultBorder(fourthEmpty);
        setTextViewDefaultTextColor(fourthEmpty);
        setEmptyTextViewDefaultState(fifthEmpty,"30");
        setTextViewDefaultBorder(fifthEmpty);
        setTextViewDefaultTextColor(fifthEmpty);
        setEmptyTextViewDefaultState(sixthEmpty,"31");
        setTextViewDefaultBorder(sixthEmpty);
        setTextViewDefaultTextColor(sixthEmpty);

        calenderLastLine.setVisibility(View.GONE);
        emptyLayOut.setVisibility(View.GONE);
    }

    public void setNumberOfEmptyTextView(TextView myTextView,int addNumber){
        myTextView.setText(String.valueOf(Integer.parseInt(myTextView.getText().toString())+addNumber));
        myTextView.setVisibility(View.VISIBLE);
        setTextViewBorder(myTextView);
    }

    public void makeNotUsedTextViewInvisible(TextView textView){   //make textView invisible if we dont need its day on the calender
        if(Integer.parseInt(textView.getText().toString())>=mViewModel.itemsList.size()+1)
            textView.setVisibility(View.INVISIBLE);
    }


    public void setNumberOfTextViews(String minusNumber){
        setNumberText(one,minusNumber);  //to set the one textView text one-minus number(if its number becomes 0 or negetive its text witll set to "")
        setTextViewBorder(one);
        setNumberText(two,minusNumber);
        setTextViewBorder(two);
        setNumberText(three,minusNumber);
        setTextViewBorder(three);
        setNumberText(four,minusNumber);
        setTextViewBorder(four);
        setNumberText(five,minusNumber);
        setTextViewBorder(five);
        setNumberText(six,minusNumber);
        setTextViewBorder(six);
        setNumberText(seven,minusNumber);
        setTextViewBorder(seven);
        setNumberText(eight,minusNumber);
        setTextViewBorder(eight);
        setNumberText(nine,minusNumber);
        setTextViewBorder(nine);
        setNumberText(ten,minusNumber);
        setTextViewBorder(ten);
        setNumberText(eleven,minusNumber);
        setTextViewBorder(eleven);
        setNumberText(twelve,minusNumber);
        setTextViewBorder(twelve);
        setNumberText(thirteen,minusNumber);
        setTextViewBorder(thirteen);
        setNumberText(fourteen,minusNumber);
        setTextViewBorder(fourteen);
        setNumberText(fifteen,minusNumber);
        setTextViewBorder(fifteen);
        setNumberText(sixteen,minusNumber);
        setTextViewBorder(sixteen);
        setNumberText(seventeen,minusNumber);
        setTextViewBorder(seven);
        setNumberText(eighteen,minusNumber);
        setTextViewBorder(eighteen);
        setNumberText(nineteen,minusNumber);
        setTextViewBorder(nineteen);
        setNumberText(twenty,minusNumber);
        setTextViewBorder(twenty);
        setNumberText(twentyOne,minusNumber);
        setTextViewBorder(twentyOne);
        setNumberText(twentyTwo,minusNumber);
        setTextViewBorder(twentyTwo);
        setNumberText(twentyThree,minusNumber);
        setTextViewBorder(twentyThree);
        setNumberText(twentyFour,minusNumber);
        setTextViewBorder(twentyFour);
        setNumberText(twentyFive,minusNumber);
        setTextViewBorder(twentyFive);
        setNumberText(twentySix,minusNumber);
        setTextViewBorder(twentySix);
        setNumberText(twentySeven,minusNumber);
        setTextViewBorder(twentySeven);
        setNumberText(twentyEight,minusNumber);
        setTextViewBorder(twentyEight);
        setNumberText(twentyNine,minusNumber);
        setTextViewBorder(twentyNine);
        setNumberText(thirty,minusNumber);
        setTextViewBorder(thirty);
        setNumberText(thirtyOne,minusNumber);
        setTextViewBorder(thirtyOne);

    }


    public void setData(){
        setDefaultCalender();   //to set the default calender(first day of the month is in monday and empty textViews are invisible and last line of calender is invisible)
        totalTime.setText(mViewModel.totalTime);
        vacationTime.setText(mViewModel.vacationTime);
        extraTime.setText(mViewModel.extraTime);
        delayTime.setText(mViewModel.delayTime);
        usdProfit.setText(mViewModel.usdProfit);
        irrProfit.setText(mViewModel.irrProfit);
        if(mViewModel.selectedMonth.getValue().equals("")){
            totalTimeText.setText("ساعت های کاری در "+ Assistance.defineMonthName(mViewModel.activeMonth));
            vacationTimeText.setText("ساعت های مرخصی در "+ Assistance.defineMonthName(mViewModel.activeMonth));
            delayTimeText.setText("میزان تاخیر در "+ Assistance.defineMonthName(mViewModel.activeMonth));
        }
        else{
            totalTimeText.setText("ساعت های کاری در "+ Assistance.defineMonthName(mViewModel.selectedMonth.getValue()));
            vacationTimeText.setText("ساعت های مرخصی در "+ Assistance.defineMonthName(mViewModel.selectedMonth.getValue()));
            delayTimeText.setText("میزان تاخیر در "+ Assistance.defineMonthName(mViewModel.selectedMonth.getValue()));
        }

        switch (mViewModel.dayOfTheWeek){  //to set calender

            case"0":    //so we should have the default calender and  the 1th day of the month is in monday
                setNumberOfTextViews("0");
                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);

                break;


            case "1":   //so th 1th day of the month is in tuesday

                setNumberOfTextViews("1");   //to set number of 31 first textViews by minusing the dayOfTheWeek number from them
                setNumberOfEmptyTextView(firstEmpty,5);  //to make first empty textView visible and set its number to 31(26+5=31)

                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);
                makeNotUsedTextViewInvisible(firstEmpty);

                emptyLayOut.setVisibility(View.VISIBLE);
                calenderLastLine.setVisibility(View.GONE);   //we dont need this line yet

                break;

            case "2" :   //so the 1th day of the month is in wednesday

                setNumberOfTextViews("2");
                setNumberOfEmptyTextView(firstEmpty,4);
                setNumberOfEmptyTextView(secondEmpty,4);

                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);
                makeNotUsedTextViewInvisible(firstEmpty);
                makeNotUsedTextViewInvisible(secondEmpty);

                emptyLayOut.setVisibility(View.VISIBLE);
                calenderLastLine.setVisibility(View.GONE);

                break;


            case "3":   //so the 1th day of the month is in thursday

                setNumberOfTextViews("3");
                setNumberOfEmptyTextView(firstEmpty,3);
                setNumberOfEmptyTextView(secondEmpty,3);
                setNumberOfEmptyTextView(thirdEmpty,3);

                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);
                makeNotUsedTextViewInvisible(firstEmpty);
                makeNotUsedTextViewInvisible(secondEmpty);
                makeNotUsedTextViewInvisible(thirdEmpty);

                emptyLayOut.setVisibility(View.VISIBLE);
                calenderLastLine.setVisibility(View.GONE);

                break;


            case "4":   //so the 1th day of the month is in friday

                setNumberOfTextViews("4");
                setNumberOfEmptyTextView(firstEmpty,2);
                setNumberOfEmptyTextView(secondEmpty,2);
                setNumberOfEmptyTextView(thirdEmpty,2);
                setNumberOfEmptyTextView(fourthEmpty,2);

                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);
                makeNotUsedTextViewInvisible(firstEmpty);
                makeNotUsedTextViewInvisible(secondEmpty);
                makeNotUsedTextViewInvisible(thirdEmpty);
                makeNotUsedTextViewInvisible(fourthEmpty);

                emptyLayOut.setVisibility(View.VISIBLE);
                calenderLastLine.setVisibility(View.GONE);

                break;


            case "5":   //so the 1th day of the month is in saturday

                setNumberOfTextViews("5");
                setNumberOfEmptyTextView(firstEmpty,1);
                setNumberOfEmptyTextView(secondEmpty,1);
                setNumberOfEmptyTextView(thirdEmpty,1);
                setNumberOfEmptyTextView(fourthEmpty,1);
                setNumberOfEmptyTextView(fifthEmpty,1);

                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);
                makeNotUsedTextViewInvisible(firstEmpty);
                makeNotUsedTextViewInvisible(secondEmpty);
                makeNotUsedTextViewInvisible(thirdEmpty);
                makeNotUsedTextViewInvisible(fourthEmpty);
                makeNotUsedTextViewInvisible(fifthEmpty);

                if(fifthEmpty.getVisibility() == View.VISIBLE) {
                    emptyLayOut.setVisibility(View.GONE);
                    calenderLastLine.setVisibility(View.VISIBLE);
                }
                else{
                    emptyLayOut.setVisibility(View.VISIBLE);
                    calenderLastLine.setVisibility(View.GONE);
                }
                break;


            case "6":  //so the 1th day of the month is in sunday

                setNumberOfTextViews("6");
                setNumberOfEmptyTextView(firstEmpty,0);
                setNumberOfEmptyTextView(secondEmpty,0);
                setNumberOfEmptyTextView(thirdEmpty,0);
                setNumberOfEmptyTextView(fourthEmpty,0);
                setNumberOfEmptyTextView(fifthEmpty,0);
                setNumberOfEmptyTextView(sixthEmpty,0);

                makeNotUsedTextViewInvisible(thirty);
                makeNotUsedTextViewInvisible(thirtyOne);
                makeNotUsedTextViewInvisible(firstEmpty);
                makeNotUsedTextViewInvisible(secondEmpty);
                makeNotUsedTextViewInvisible(thirdEmpty);
                makeNotUsedTextViewInvisible(fourthEmpty);
                makeNotUsedTextViewInvisible(fifthEmpty);
                makeNotUsedTextViewInvisible(sixthEmpty);

                if(fifthEmpty.getVisibility() == View.VISIBLE) {
                    emptyLayOut.setVisibility(View.GONE);
                    calenderLastLine.setVisibility(View.VISIBLE);
                }
                else{
                    emptyLayOut.setVisibility(View.VISIBLE);
                    calenderLastLine.setVisibility(View.GONE);
                }

                break;
        }


    }
}