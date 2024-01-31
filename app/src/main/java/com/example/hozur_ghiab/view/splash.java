package com.example.hozur_ghiab.view;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.example.hozur_ghiab.view_model.SplashViewModel;



public class splash extends Fragment {

    private SplashViewModel mViewModel;
    View view;
    SharedPrefManager sharedPrefManager;


    public void myInit(){
        sharedPrefManager=new SharedPrefManager(getActivity());
    }

    public void enterApp(){
        new Handler().postDelayed(new Runnable() {             //to show splash screen for 3000 ms
            @Override
            public void run() {
                //This method will be executed once the timer is over
                // Start your app with one of the fragments
                if(!sharedPrefManager.getToken().equals("")){  //so user is loged in

                    Navigation.findNavController(view).navigate(R.id.go_dashboard);

                } else{                                        //so user is not loged in
                    Navigation.findNavController(view).navigate(R.id.go_login);
                }
            }
        }, 1000);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.splash_fragment, container, false);
        myInit();
        enterApp();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }
}