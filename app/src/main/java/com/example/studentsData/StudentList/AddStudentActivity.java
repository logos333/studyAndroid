package com.example.studentsData.StudentList;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.text.format.DateUtils;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {

    SeekBar seekBar;
    Button saveBtn, deleteBtn;
    TextView birthTxt, languageTxt, idTxt, nameTxt;
    RadioButton male, female;

    Calendar date = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        birthTxt = findViewById(R.id.birthTxt);
        seekBar = findViewById(R.id.seekBar);
        languageTxt = findViewById(R.id.languageTxt);
        saveBtn = findViewById(R.id.saveBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        idTxt = findViewById(R.id.idTxt);
        nameTxt = findViewById(R.id.nameTxt);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);

        seekBar.setOnSeekBarChangeListener(seekBar_Change);

        Student student;
        if((student = (Student) getIntent().getSerializableExtra("student")) != null){
           initialData(student);
        }
    }

    private void initialData(Student student) {
        //setting data to fields
        // deleteBtn will be visible when user will enter to this activity from tapping on the ListItem
        //and the information about the ListItem student will displayed in the controls of this activity
        //and then user will be able to modify (except student's id) and DELETE information about ListItem(student)

        deleteBtn.setVisibility(View.VISIBLE);
        //setting id
        idTxt.setText(student.getSid());
        idTxt.setEnabled(false);
        //setting name
        nameTxt.setText(student.getSname());
        //setting sex
        female.setChecked(student.getSsex().equals("female"));
        //setting date of birth
        birthTxt.setText(student.getDateOfBirth());
        //setting languages seekBar
        seekBar.setProgress(student.getLanguages());
    }


    SeekBar.OnSeekBarChangeListener seekBar_Change = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            languageTxt.setText("Languages: " + String.valueOf(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    public void dateOfBirthBtn_Click(View view) {
        new DatePickerDialog(this, d,
                date.get(Calendar.YEAR),
                date.get(Calendar.MONTH),
                date.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.set(Calendar.YEAR, year);
            date.set(Calendar.MONTH, monthOfYear);
            date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {
        if (date.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            MyToast.showMessage(this, "BirthDay cannot be in the future!");
            return;
        }
        birthTxt.setText(DateUtils.formatDateTime(this,
                date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    //saveBtn
    public void saveBtn_Click(View view) {
        try {
            check();
            save();

        } catch (Exception ex) {
            MyToast.showMessage(this, ex.getMessage());
        }
    }

    protected void check() throws Exception {
        if (idTxt.getText().toString().equals("")) {
            throw new Exception("ID cannot be empty!");
        }
        if (nameTxt.getText().toString().equals("")) {
            throw new Exception("Name cannot be empty!");
        }
        if (birthTxt.getText().toString().equals("")) {
            throw new Exception("Birth day cannot be empty!");
        }


    }

    protected void save() throws Exception{
        Student student = new Student(idTxt.getText().toString(),
                nameTxt.getText().toString(),
                male.isChecked() ? "male" : "female",
                birthTxt.getText().toString(),
                seekBar.getProgress());

        addStudent(student);

        backToPreviousActivity();
    }

    protected void addStudent(Student student){
        try {
            if(deleteBtn.isEnabled())
                DataControl.deleteStudent(student.getSid());
            DataControl.addStudent(student);
        } catch (Exception ex) {
            MyToast.showMessage(this, ex.getMessage());
        }
    }

    public void deleteBtn_Click(View view) {
        try {
            if(DataControl.deleteStudent(idTxt.getText().toString()))
                MyToast.showMessage(this, "DELETED!");
            else
                MyToast.showMessage(this, "Can't find student with id = "+idTxt.getText().toString());

            backToPreviousActivity();
        } catch (Exception ex) {
            MyToast.showMessage(this, ex.getMessage());
        }
    }

    private void backToPreviousActivity() {
        Intent intent = new Intent(this, StudentListActivity.class);
        //clear stack of activities
        intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra("new student", student);
        startActivity(intent);
    }
}
