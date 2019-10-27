package com.example.studentsData.register;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class User {

    private String login, password, rePassword;
    private Set<String> data = new HashSet<>();


    public Set<String> getData() {
        data.add(password);
        data.add(Calendar.getInstance().getTime().toString());
        return data;
    }

    public String getLogin() {
        return login;
    }

    protected void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    protected void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }


}
