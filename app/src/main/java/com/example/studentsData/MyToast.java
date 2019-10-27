package com.example.studentsData;

import android.content.Context;
import android.widget.Toast;

public abstract class MyToast {
    public static void showMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
