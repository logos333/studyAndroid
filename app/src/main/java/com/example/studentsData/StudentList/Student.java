package com.example.studentsData.StudentList;

import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    private String sid;
    private String sname;
    private String ssex;
    private String dateOfBirth;


    private int languages;

    public Student(String sid, String sname, String ssex, String dateOfBirth, int languages) {
        this.sid = sid;
        this.sname = sname;
        this.ssex = ssex;
        this.dateOfBirth = dateOfBirth;
        this.languages = languages;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSsex() {
        return ssex;
    }

    public void setSsex(String ssex) {
        this.ssex = ssex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getLanguages() {
        return languages;
    }

    public void setLanguages(int languages) {
        this.languages = languages;
    }
}