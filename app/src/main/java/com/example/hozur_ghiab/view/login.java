package com.example.hozur_ghiab.view;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.hozur_ghiab.my_class.VolleyErrorHelper;
import com.example.hozur_ghiab.view_model.LoginViewModel;
import com.example.hozur_ghiab.R;

public class login extends Fragment {

    private LoginViewModel mViewModel;
    View view;
    EditText phoneNumber,pass;
    Button loginBtn;
    ProgressBar progressBar;
    private Toast m_currentToast;
    CustomProgressDialog progress ;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.login_fragment, container, false);
        myInit();          //to initialize variables
        listener();        //for onClick methods
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);         //to initialize viewModel
        mViewModel.message.setValue("");

        mViewModel.progressFinished.observe(getActivity(),state->{     //to make progressbar invisible if we get response from server
            progress.dismiss();
        });
        mViewModel.message.observe(getActivity(),message->{            //to show Toast message if needed
            if(!mViewModel.message.getValue().equals("")){
            progress.dismiss();
            showToast(mViewModel.message.getValue());
            if(mViewModel.message.getValue().equals("شما با موفقیت وارد شدید."))   //to go to dashboard if we have logined successfully
                Navigation.findNavController(view).navigate(R.id.go_dashboard);
            }
            else{
            }
        });
    }


    public void myInit(){
        phoneNumber=view.findViewById(R.id.phone_number);   //editText for phoneNumber
        pass=view.findViewById(R.id.password);              //editText for password
        loginBtn=view.findViewById(R.id.login_btn);         //button for login
        progress = new CustomProgressDialog(getActivity());       //my customProgressDialog to show progress
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    //to set style for progress dialog
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
        m_currentToast.show();                       //to show Toast message
    }}


    public void listener(){
        loginBtn.setOnClickListener(new View.OnClickListener(){      //login button onClick listener
            @Override
            public void onClick(View view) {
                progress.show();             //to show the progressDialog
                mViewModel.loginUser(phoneNumber.getText().toString(),pass.getText().toString(),getContext(),getActivity());   //to login user
            }
        });
    }

}