package com.example.hozur_ghiab.view;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.hozur_ghiab.R;

public class DescriptionDialog extends Dialog{

        public Activity c;
        public Dialog d;
        public String mydescriptionText;      //descriptionText to show in dialog
        TextView myDescriptionTextView;           //text view to show description

        public DescriptionDialog(Activity a,String descriptionText) {     //constructor
            super(a);
            this.c = a;
            mydescriptionText=descriptionText;
        }

        public void myInit(){           //to initialize variables
            myDescriptionTextView=findViewById(R.id.description_text_view);
            setText(mydescriptionText);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.description_dialog);        //to set the xml layout for this class.
            myInit();
        }


        public void setText(String myDescription){             //to set description  to textView
                    myDescriptionTextView.setText(myDescription);
        }
}

