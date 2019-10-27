package com.example.studentsData.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

public class RegisterActivity extends AppCompatActivity {

    public static final String REGISTER_FILE = "USERS";

    String login, password, rePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void _registerBtn_onClick(View view) {
        try {
            login = ((EditText)findViewById(R.id.loginTxt)).getText().toString().trim();
            password = ((EditText)findViewById(R.id.passwordTxt)).getText().toString();
            rePassword = ((EditText)findViewById(R.id.rePasswordTxt)).getText().toString();

            new UserLogic(login, password, rePassword, this);

            MyToast.showMessage(getApplicationContext(), "Register successfully!");

            super.onBackPressed();

        } catch (Exception e) {
            MyToast.showMessage(getApplicationContext(), e.getMessage());
        }
    }




}
