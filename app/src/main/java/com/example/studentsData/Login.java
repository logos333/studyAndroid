package com.example.studentsData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.studentsData.register.RegisterActivity;

import java.util.HashSet;
import java.util.Set;

public class Login {
    private String login;
    private String password;
    private Activity prevActivity;
    private Class nextActivity;
    private static Login user;

    private static final String EXCEPTION_MESSAGE_LOGIN = "Login is incorrect";
    private static final String EXCEPTION_MESSAGE_PASSWORD = "Password is incorrect";
    private static final String EXCEPTION_EMPTY = "Fields cannot be empty!";


    private Login(Activity prevActivity, Class nextActivity, String login, String password) {
        this.login = login;
        this.password = password;
        this.prevActivity = prevActivity;
        this.nextActivity = nextActivity;
    }


    public static Login check(Activity prevActivity, Class nextActivity, String login, String password) throws Exception {
        //if fields are empty
        if(login.equals("") || password.equals(""))
            throw new Exception(EXCEPTION_EMPTY);

        //open the Preferences file
        SharedPreferences pref = prevActivity.getSharedPreferences(RegisterActivity.REGISTER_FILE, Context.MODE_PRIVATE);

        //if there is no such login throw exception
        if (!pref.contains(login))
            throw new Exception(EXCEPTION_MESSAGE_LOGIN);

        //get password from file
        Set<String> data = pref.getStringSet(login, new HashSet<String>());

        //check password of this login
        if(!data.contains(password))
            throw new Exception(EXCEPTION_MESSAGE_PASSWORD);

        return user = new Login(prevActivity, nextActivity, login, password);
    }

    public void enterToActivity() {
        //if everything is ok, than start new Activity
        Intent intent = new Intent(prevActivity, nextActivity);
        //intent.putExtra(MainActivity.EXTRA_MESSAGE, login);
        prevActivity.startActivity(intent);
    }
}
