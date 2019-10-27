package com.example.studentsData;

import androidx.appcompat.app.AppCompatActivity;

import android.view.ContextMenu;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.studentsData.StudentList.StudentListActivity;
import com.example.studentsData.register.RegisterActivity;

public class MainActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public void signIn_onClick(View view) {
        String login = ((EditText) findViewById(R.id.loginTxt)).getText().toString();
        String password = ((EditText) findViewById(R.id.passwordTxt)).getText().toString();

        try {
            //get login and password than enter to new activity
            Login.check(MainActivity.this, StudentListActivity.class, login, password).enterToActivity();
        } catch (Exception ex) {
            //MyToast.showMessage(getApplicationContext(), ex.getMessage());
            MySnackbar.showMessage(view, ex.getMessage());
        }

    }

    public void registerBtn_onClick(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

}

