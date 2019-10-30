package com.example.studentsData.StudentList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.text.format.DateUtils;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {

    SeekBar seekBar;
    Button saveBtn, deleteBtn;
    TextView birthTxt, languageTxt, idTxt, nameTxt;
    RadioButton male, female;
    File directory;
    ImageButton imageButton;
    Uri outputFileUri;

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
        imageButton = findViewById(R.id.imageButton);

        seekBar.setOnSeekBarChangeListener(seekBar_Change);

        Student student;
        if ((student = (Student) getIntent().getSerializableExtra("student")) != null) {
            initialData(student);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

        if (outputFileUri == null)
            outputFileUri = Uri.parse(getSharedPreferences("Preferences", MODE_PRIVATE).getString("image", null));

        outState.putString("imageButton", outputFileUri.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getString("imageButton") != null)
            imageButton.setImageURI(Uri.parse(savedInstanceState.getString("imageButton")));

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
        //setting image
        imageButton.setImageURI(Uri.parse(student.getPhoto()));
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

    protected void save() {
        Student student = new Student(idTxt.getText().toString(),
                nameTxt.getText().toString(),
                male.isChecked() ? "male" : "female",
                birthTxt.getText().toString(),
                seekBar.getProgress(),
                getSharedPreferences("Preferences", MODE_PRIVATE).getString("image", null));

        addStudent(student);

        backToPreviousActivity();
    }

    protected void addStudent(Student student) {
        try {
            if (deleteBtn.isEnabled())
                DataControl.deleteStudent(student.getSid());
            DataControl.addStudent(student);
        } catch (Exception ex) {
            MyToast.showMessage(this, ex.getMessage());
        }
    }

    public void deleteBtn_Click(View view) {
        try {
            if (DataControl.deleteStudent(idTxt.getText().toString()))
                MyToast.showMessage(this, "DELETED!");
            else
                MyToast.showMessage(this, "Can't find student with id = " + idTxt.getText().toString());

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

    /**
     * photo button
     */
    private final int CAMERA = 1;
    private final int GALLERY = 2;

    public void onClickPhoto(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select source:");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri = generateFileUri());
                startActivityForResult(intent, CAMERA);
            }
        });
        builder.setNeutralButton("Gallery", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
            }
        });
        builder.show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                applyImage();

            } else if (resultCode == RESULT_CANCELED) {
                Log.d("MyLogs", "Canceled");
            }
            return;
        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            if (data == null) {
                MyToast.showMessage(this, "Error!");
                return;
            }
            outputFileUri = data.getData();
            applyImage();
        }

    }

    private void applyImage() {
        try {
            /*Bitmap bitmap = scaleDown(MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri), 120, true);
            new File(outputFileUri.toString()).delete();

            File file = new File(generateFileUri().toString());
            FileOutputStream fos = new FileOutputStream(file);
           // file.createNewFile();
            outputFileUri = Uri.fromFile(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
*/
            Bitmap bitmap = scaleDown(MediaStore.Images.Media.getBitmap(this.getContentResolver(), outputFileUri), 1200, true);

            String path = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/photo_"
                    + System.currentTimeMillis() + ".jpg";

            FileOutputStream fos = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            outputFileUri = Uri.parse(path);

            fos.flush();
            fos.close();

        } catch (Exception ex) {
            MyToast.showMessage(this, ex.getMessage());
        }


        getSharedPreferences("Preferences", MODE_PRIVATE).edit().putString("image", outputFileUri.toString()).apply();
        Log.d("MyLogs", outputFileUri.toString());
        imageButton.setImageURI(outputFileUri);
    }

    private Bitmap scaleDown(@NonNull Bitmap realImage, float maxImageSize,
                             boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    private Uri generateFileUri() {
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "photo_"
                + System.currentTimeMillis() + ".jpg");
        Log.d("MyLogs", "fileName = " + file);
        return Uri.fromFile(file);
    }


}
