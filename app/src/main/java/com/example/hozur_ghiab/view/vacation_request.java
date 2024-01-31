package com.example.hozur_ghiab.view;

import androidx.lifecycle.ViewModelProvider;


import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.my_class.Assistance;
import com.example.hozur_ghiab.my_class.Roozh;
import com.example.hozur_ghiab.view_model.VacationRequestViewModel;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;


public class vacation_request extends Fragment {

    private VacationRequestViewModel mViewModel;
    View view;
    ImageView hamburgerIcon;
    EditText vacationSubject,vacationDescription;
    TextView vacationDate,vacationEndTime,vacationStartTime,vacationStartDate,vacationEndDate;
    Button okButton;
    RadioGroup radioGroup;
    RadioButton hourlyVacationRadioButton,dailyVacationRadioButton;
    LinearLayout vacationTimeLayout;
    Toast m_currentToast;
    PersianDatePickerDialog picker;
    String typeOfVacation,vacationDateString,vacationStartTimeString,vacationEndTimeString,
            vacationStartDateString,vacationEndDateString,today,now;
    Calendar calendar;
    CustomProgressDialog progress ;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.vacation_request_fragment, container, false);
        myInit();
        radioGroupDefaultMode();
        listener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(VacationRequestViewModel.class);

        mViewModel.message.setValue("");

        mViewModel.message.observe(getActivity(),state->{     //to show ToastMessage if needed
            if(!mViewModel.message.getValue().equals("")){
                progress.dismiss();
                showToast(mViewModel.message.getValue());}
            else{

            }
        });

        mViewModel.finishProgress.observe(getActivity(),state->{     //to  hide progress dialog if needed
            if(mViewModel.finishProgress.getValue()){
                progress.dismiss();

                if(mViewModel.vacationSubmited){
                makeFieldsEmpty();
                mViewModel.vacationSubmited=false;
            }}
        });
    }

    public void myInit() {
        hamburgerIcon = view.findViewById(R.id.hamburger_icon);
        vacationSubject = view.findViewById(R.id.vacation_subject);
        vacationDescription = view.findViewById(R.id.vacation_description);
        vacationDate = view.findViewById(R.id.vacation_date);
        vacationEndTime = view.findViewById(R.id.vacation_end_time);
        vacationStartTime = view.findViewById(R.id.vacation_start_time);
        vacationStartDate = view.findViewById(R.id.vacation_start_date);
        vacationEndDate = view.findViewById(R.id.vacation_end_date);
        okButton = view.findViewById(R.id.ok_button);
        radioGroup = view.findViewById(R.id.radio_group);
        vacationTimeLayout = view.findViewById(R.id.vacation_time_layout);
        hourlyVacationRadioButton = radioGroup.findViewById(R.id.hourly_vacation);
        dailyVacationRadioButton = radioGroup.findViewById(R.id.daily_vacation);
        calendar = Calendar.getInstance();

        progress = new CustomProgressDialog(getActivity());       //my customProgressDialog to show progress
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    //to set style for progress dialog

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(date);          //today date in format yyyy-mm-dd
    }

    public void showToast(String text) {
        if (!text.equals("")) {
            if (m_currentToast != null) {
                m_currentToast.cancel();                 //not to show Toast message for several times
            }
            m_currentToast = Toast.makeText(getActivity(), text,
                    Toast.LENGTH_SHORT);
            m_currentToast.show();                       //to show Toast message
        }
    }


    public void radioGroupDefaultMode(){                              //to set dafault mode for radioGroup: hourlyVacation is checked
        setRadioButtonChecked(hourlyVacationRadioButton);
        typeOfVacation="hourly";
    }

    public void setRadioButtonChecked(RadioButton myRadioButton){          //to make radioButton checked
        myRadioButton.setButtonDrawable(R.drawable.radio_button_filled);
        myRadioButton.setChecked(true);
    }

    public void setRadioButtonUnchecked(RadioButton myRadioButton){         //to make radioButton unChecked
        myRadioButton.setButtonDrawable(R.drawable.radio_button_empty);
        myRadioButton.setChecked(false);
    }

    public void loadDailyVacation(){                          //to make textViews related to dailyVacation visible and textViews related to
                                                             // hourlyVacation Gone
        vacationEndDate.setVisibility(View.VISIBLE);
        vacationStartDate.setVisibility(View.VISIBLE);
        vacationTimeLayout.setVisibility(View.GONE);
        vacationDate.setVisibility(View.GONE);
    }

    public void loadHourlyVacation(){                           //to make textViews related to hourlyVacation visible  and textViews
                                                                 // related to dailyVacation Gone
        vacationTimeLayout.setVisibility(View.VISIBLE);
        vacationDate.setVisibility(View.VISIBLE);
        vacationStartDate.setVisibility(View.GONE);
        vacationEndDate.setVisibility(View.GONE);
    }

    public void setDatePicker(TextView myTextView){              //to set datePicker to choose date from calender
        picker = new PersianDatePickerDialog(getContext())
                .setPositiveButtonString("تایید")              //button to submit date user selected
                .setNegativeButton("انصراف")                   //button to hide datePicker
                .setTodayButton("امروز")                       //to show today date on datePicker
                .setTodayButtonVisible(true)
                .setMinYear(PersianDatePickerDialog.THIS_YEAR)   //to set minYear of datePicker : this day
                .setActionTextColor(Color.parseColor("#241577"))
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(true)
                .setListener(new PersianPickerListener() {
                    @Override
                    public void onDateSelected(@NotNull PersianPickerDate persianPickerDate) {
                        myTextView.setText(persianPickerDate.getPersianYear() + "-" + persianPickerDate.getPersianMonth() +
                            "-" + persianPickerDate.getPersianDay());   //to set the date user selected , on textView
                        if(myTextView.equals(vacationDate))
                            vacationDateString= Assistance.convertDateToMiladi(persianPickerDate.getPersianYear() ,
                                    persianPickerDate.getPersianMonth()
                                    , persianPickerDate.getPersianDay()); //to convert the date to miladi and save it as string  to send it to server
                        else if (myTextView.equals(vacationStartDate))
                            vacationStartDateString=Assistance.convertDateToMiladi(persianPickerDate.getPersianYear() ,
                                    persianPickerDate.getPersianMonth()
                                    , persianPickerDate.getPersianDay());  //to convert the date to miladi and save it as string  to send it to server
                        else if (myTextView.equals(vacationEndDate))
                            vacationEndDateString=Assistance.convertDateToMiladi(persianPickerDate.getPersianYear() ,
                                    persianPickerDate.getPersianMonth()
                                    , persianPickerDate.getPersianDay());  //to convert the date to miladi and save it as string  to send it to server
                    }
                    @Override
                    public void onDismissed() {
                    }
                });
        picker.show();        //to show the date picker
    }

    public void setTimePicker(TextView myTextView){

        TimePickerDialog mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                myTextView.setText(mViewModel.prepareTime(selectedHour,selectedMinute));          //to set the selected time on related textView

                now=mViewModel.prepareTime( calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE)); //to save now time in format hh:mm in string;

                if(myTextView.equals(vacationStartTime))
                    vacationStartTimeString=mViewModel.prepareTime(selectedHour,selectedMinute);            //to save the selected time in string
                                                                                                            // to send it to server
                else if(myTextView.equals(vacationEndTime))
                    vacationEndTimeString=mViewModel.prepareTime(selectedHour,selectedMinute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        if(myTextView.equals(vacationStartTime))
            mTimePicker.setTitle(" زمان شروع مرخصی را انتخاب کنید"+"\n");   //to set title for time picker

        else if(myTextView.equals(vacationEndTime))
            mTimePicker.setTitle("زمان پایان مرخصی را انتخاب کنید"+"\n");   //to set title for time picker

        mTimePicker.show();        //to show time picker
    }

    public void makeEmpty(EditText myEditText){    //to make EditText empty
        myEditText.setText("");
    }

    public void setDefaultText(TextView myTextView,String text){   //to set default text for textView to make it empty
        myTextView.setText(text);
    }

    public void makeFieldsEmpty(){
        makeEmpty(vacationSubject);
        makeEmpty(vacationDescription);
        vacationDateString=null;
        vacationEndTimeString=null;
        vacationStartTimeString=null;
        vacationEndDateString=null;
        vacationStartDateString=null;
        setDefaultText(vacationDate,"تاریخ مرخصی");
        setDefaultText(vacationStartTime,"ساعت شروع");
        setDefaultText(vacationEndTime,"ساعت پایان");
        setDefaultText(vacationStartDate,"تاریخ شروع");
        setDefaultText(vacationEndDate,"تاریخ پایان");
    }

    public void listener(){

        hamburgerIcon.setOnClickListener(new View.OnClickListener() {         //to open navigation drawer when we click the hamburger icon
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).openDrawer();        //to open Navigation Drawer
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.show();
                if(typeOfVacation.equals("hourly")){    //hourly type of vacation request
                    try {
                        if (mViewModel.checkHourlyValidation(vacationSubject.getText().toString(),
                                vacationDescription.getText().toString(),vacationDateString,
                                vacationStartTimeString,vacationEndTimeString,today,now)){  //if all fields are filled correctly
                    mViewModel.hourlyVacationRequest(getContext(),getActivity(),vacationDateString,
                            vacationStartTimeString,vacationEndTimeString,
                            vacationSubject.getText().toString(),vacationDescription.getText().toString());  //to send hourly vacation Request
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                else if(typeOfVacation.equals("daily")){                                   //daily type of vacation request
                    try {
                        if(mViewModel.checkDailyValidation(vacationSubject.getText().toString(),vacationDescription.getText().toString()
                                ,vacationStartDateString,vacationEndDateString,today)){ //if all fields are filled correctly
                        mViewModel.dailyVacationRequest(getContext(),getActivity(),vacationStartDateString
                                ,vacationEndDateString, vacationSubject.getText().toString(),
                                vacationDescription.getText().toString()); }   //to send daily vacation request
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        );

        vacationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(vacationDate);   //to show datePicker and set the selected date in related textView
            }
        });

        vacationStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(vacationStartDate);    //to show datePicker and set the selected date in related textView
            }
        });

        vacationEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDatePicker(vacationEndDate);      //to show datePicker and set the selected date in related textView
            }
        });

        vacationStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setTimePicker(vacationStartTime);    //to show timePicker and set the selected time in related textView
            }
        });

        vacationEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimePicker(vacationEndTime);   //to show timePicker and set the selected time in related textView
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()    //to control the radioButton checks
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = radioGroup.findViewById(checkedId);    //the radioButton which is checked
                int index = radioGroup.indexOfChild(radioButton);                //index of the radioButton which is checked
                switch (index) {
                    case 0: //date vacation button
                        typeOfVacation="daily";
                        setRadioButtonUnchecked(hourlyVacationRadioButton);       //to make hourlyVacation radioButton unChecked
                        setRadioButtonChecked(dailyVacationRadioButton);         //to make dailyVacation radioButton Checked
                        loadDailyVacation();                                     //to make textViews related to dailyVacation visible
                        break;

                    case 1: // time vacation button
                        typeOfVacation="hourly";
                        setRadioButtonUnchecked(dailyVacationRadioButton);       //to make dailyVacation radioButton unChecked
                        setRadioButtonChecked(hourlyVacationRadioButton);           //to make hourlyVacation radioButton Checked
                        loadHourlyVacation();                                      //to make textViews related to hourlyVacation visible
                        break;
                }
            }
        });
    }
}