package com.example.hozur_ghiab.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hozur_ghiab.R;

public class ShowErrorDialog extends Dialog {
    public Activity c;
    public Dialog d;
    public String myErrorText;      //errorText to show in dialog
    TextView myErrorTextView;           //text view to show errorText
    Button ok;

    public ShowErrorDialog(Activity a,String errorMessage) {     //constructor
        super(a);
        this.c = a;
        myErrorText=errorMessage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_error_dialog);        //to set the xml layout for this class.
        myInit();    //to initialize variables
        listener();
    }

    public void myInit(){       //to initialize variables
        myErrorTextView=findViewById(R.id.errorMessage_text_view);
        ok=findViewById(R.id.ok_button);
        setText(myErrorText);
    }

    public void listener(){
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //to close the dialog by clicking ok button
            }
        });
    }


    public void setText(String myError){
        switch (myError){
            case ("generic_server_timeout"):
                myErrorTextView.setText("یک مشکل مرتبط با درخواست شما وجود دارد.لطفا بعدا امتحان کنید");
                break;
            case ("auth_failed"):
                myErrorTextView.setText("جزییات پیکربندی ارتباط شما ممکن است صحیح نباشد. ");
                break;
            case ("no_internet"):
                myErrorTextView.setText("شما به اینترنت متصل نیستید.لطفا به اینترنت متصل شده و دوباره امتحان کنید.");
                break;
            case ("no_network_connection"):
                myErrorTextView.setText("لطفا اتصال اینترنت خود را چک کنید.");
                break;
            case ("parsing_failed"):
                myErrorTextView.setText("فایل Json به فرمت قابل خواندن تبدیل نمیشود.");
                break;
            default: myErrorTextView.setText(myErrorText);
        }
    }
}