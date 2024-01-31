package com.example.hozur_ghiab.view;

import static androidx.navigation.ui.NavigationUI.*;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;

import com.example.hozur_ghiab.R;
import com.example.hozur_ghiab.my_class.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout drawerLayout;
    public NavController navController;
    public NavigationView navigationView;
    View headerView;
    TextView userNameTextView;
    private SharedPrefManager sharedPrefManager;
    Menu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myInit();
        setupNavigation();
    }

    public void myInit(){          //to initialize variables
        sharedPrefManager=new SharedPrefManager(getApplicationContext());
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        headerView = navigationView.getHeaderView(0);     //the header of  navigationView
        userNameTextView =  headerView.findViewById(R.id.user_name_text);   //textView in header for name of the user
        navigationView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);       //to show items of menu right
        myMenu=navigationView.getMenu();
    }

    private void setupNavigation() {
        setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
        userNameTextView.setText(sharedPrefManager.getName());
    }

    public void setUserName(String name){   //to set  username in textview in nav header
        userNameTextView.setText(name);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }

    @Override
    public void onBackPressed() {       //to open and close navigation drawer
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void openDrawer(){    //to open navigation drawer
        /*if(!drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.openDrawer(GravityCompat.END);
        }
        else{
            drawerLayout.closeDrawers();
        }*/
        drawerLayout.openDrawer(GravityCompat.END);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {     //to control selection of list items in navigation drawer

        menuItem.setChecked(true);         //to set the item which is selected checked

        int id = menuItem.getItemId();    //to get the id of the item selected

        switch (id) {

            case R.id.first:
                drawerLayout.closeDrawers();
                navController.navigate(R.id.dashboard);   //to go to dashboard fragment
                break;

            case R.id.second:

                MenuItem vacationRequestItem = myMenu.getItem(2);
                MenuItem vacationReportItem = myMenu.getItem(3);

                //to show vacationRequest and vacationReport items in the list if they are hide ,by clicking second item of list
                if(!vacationReportItem.isVisible()){
                    vacationRequestItem.setVisible(true);
                    vacationReportItem.setVisible(true);
                    vacationRequestItem.setChecked(false);
                }
                //to hide vacationRequest and vacationReport items in the list if they are shown ,by clicking second item of list
                else{
                    vacationRequestItem.setVisible(false);
                    vacationReportItem.setVisible(false);
                }
                break;

            case R.id.vacation_request_item:
                drawerLayout.closeDrawers();
                navController.navigate(R.id.vacation_request);    //to go to vacation_request fragment
                break;
            case R.id.vacation_report_item:
                drawerLayout.closeDrawers();
                navController.navigate(R.id.vacation_report);      //to go to vacation_report fragment
                break;
            case R.id.third:
                drawerLayout.closeDrawers();
                navController.navigate(R.id.reports);
                break;

            case R.id.forth:
                drawerLayout.closeDrawers();
                navController.navigate(R.id.wage_report);
                break;

            case R.id.fifth:
                drawerLayout.closeDrawers();
                ChangePasswordDialog myDialog=new ChangePasswordDialog(MainActivity.this,getApplicationContext());

                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));  //to set style for dialog

                myDialog.show();  //to show dialog

                break;

            case R.id.sixth:
                drawerLayout.closeDrawers();
                ExitDialog myExitDialog=new ExitDialog(MainActivity.this,getApplicationContext());

                myExitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.alpha(1)));  //to set style for dialog

                myExitDialog.show();  //to show dialog

                break;
        }


        return true;

    }

}