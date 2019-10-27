package com.example.studentsData.register;

import android.app.Activity;

public class UserLogic extends User {

    Activity activity;

    public UserLogic(String login, String password, String rePassword, Activity activity) throws Exception {
        setLogin(login);
        setPassword(password);
        setRePassword(rePassword);

        this.activity = activity;

        check();
        write();
    }

    // this method provides checking login, password and rePassword variables and if one of them are not suitable to
    // requirements than method will throw some Exception
    private void check() throws Exception {

        //all variables are empty
        if (getLogin().equals("") || getPassword().equals("") || getRePassword().equals(""))
            throw new Exception("Fields cannot be empty!");

        //length < 3
        if (getLogin().length() < 3)
            throw new Exception("Login need to be at least 3 characters!");
        if (getPassword().length() < 3)
            throw new Exception("Password need to be at least 3 characters!");

        //passwords are not equal
        if (!getPassword().equals(getRePassword()))
            throw new Exception("Passwords are not equal!");

        //login already exists
        if(activity.getSharedPreferences(RegisterActivity.REGISTER_FILE, 0).contains(getLogin()))
            throw  new Exception("This login has already taken! Please input another one");

    }

    private void write() {
        activity.getSharedPreferences(RegisterActivity.REGISTER_FILE, 0).edit().putStringSet(getLogin(), getData()).apply();
    }

}
