package com.example.studentsData;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public abstract class MySnackbar {
    public static void showMessage(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
